package models.view

import DataPreferences
import LogConsole
import androidx.compose.material.Colors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cleanMotdText
import kotlinx.coroutines.*
import managers.FavoriteStorage
import managers.NotifierManager
import managers.ProfileStorage
import managers.ProfileStorage.saveProfiles
import models.IpPortsRange
import models.Profile
import models.ServerInfo
import scanServers
import scaneServer
import stripFormatting
import themes.AppTheme
import themes.ThemeState
import toHex
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(val themeState: ThemeState) {
    private val favoriteMonitoringJobs = mutableMapOf<String, Job>()
    private val htmlFileDateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm")
    private val logDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    val appDataDir = when {
        System.getProperty("os.name").startsWith("Windows") ->
            System.getenv("APPDATA") ?: System.getProperty("user.home")
        else ->
            System.getenv("XDG_CONFIG_HOME") ?: "${System.getProperty("user.home")}/.config"
    }
    private val offlineServersDir = File(appDataDir, "MinecraftServerScanner")
    
    var timeoutMs by mutableStateOf(500)
    var parallelLimit by mutableStateOf(Runtime.getRuntime().availableProcessors() * 50)
    var servers by mutableStateOf(listOf<ServerInfo>())
    var players by mutableStateOf(listOf<String>())
    var scanning by mutableStateOf(false)
    var stopScan by mutableStateOf(false)
    var currentAction by mutableStateOf("")
    var scanProgress by mutableStateOf(0f)
    var filterText by mutableStateOf("")
    var filterVersion by mutableStateOf("")
    var filterCountry by mutableStateOf("")
    var filterServerType by mutableStateOf("")
    var showSettings by mutableStateOf(false)
    var isMapDialogVisible by mutableStateOf(false)
    var mapLink by mutableStateOf("")
    var profiles by mutableStateOf(loadProfiles())
    var currentProfileIndex by mutableStateOf(0)
    var targetIps by mutableStateOf(profiles.getOrNull(currentProfileIndex)?.targetIps ?: emptyList())
    var currentTimeout by mutableStateOf(profiles.getOrNull(currentProfileIndex)?.timeout ?: timeoutMs)
    var currentParallelLimit by mutableStateOf(profiles.getOrNull(currentProfileIndex)?.parallels ?: parallelLimit)
    var currentTheme by mutableStateOf(profiles.getOrNull(currentProfileIndex)?.theme ?: AppTheme.MINECRAFT)


    private val _favorites = mutableStateListOf<String>()
    val favorites: List<String> = _favorites

    fun addFavorite(server: ServerInfo, colors: Colors) {

        val desc = stripFormatting(server.motd.substringBefore("by")).trim()

        if (desc.isNotBlank() && !_favorites.contains(desc)) {
            _favorites.add(desc)
            saveFavoritesAsync()
            startMonitoringFavorite(server, colors)
        }
    }

    fun removeFavorite(server: ServerInfo) {

        val desc = stripFormatting(server.motd.substringBefore("by")).trim()

        if (_favorites.remove(desc)) {
            saveFavoritesAsync()
            stopMonitoringFavorite(desc)
        }
    }

    fun isFavorite(desc: String): Boolean {
        return _favorites.contains(desc)
    }

    private fun loadFavorites() {
        val loaded = FavoriteStorage.loadFavorites()
        _favorites.clear()
        _favorites.addAll(loaded)
    }

    private fun saveFavoritesAsync() {
        CoroutineScope(Dispatchers.IO).launch {
            FavoriteStorage.saveFavorites(_favorites)
        }
    }

    private fun saveMotdDescription(server: ServerInfo) {
        try {
            val rawMotd = stripFormatting(server.motd)
            val desc = rawMotd.substringBefore("by").trim()
            val file = File("offline_servers/${server.ip}_${server.port}.txt")
            file.parentFile.mkdirs()
            file.writeText(desc)
            LogConsole.debug("Возможная карта сохранена: ${file.absolutePath}")
        } catch (e: Exception) {
            LogConsole.error("Ошибка при сохранении MOTD: ${e.message}")
        }
    }

    private val htmlFile: File
        get() {
            offlineServersDir.mkdirs()
            val filename = "${htmlFileDateFormat.format(Date())}.html"
            return File(offlineServersDir, filename)
        }

    fun saveFaviconToFile(server: ServerInfo): String? {
        return try {
            val faviconData = server.favicon
            val base64Str = faviconData.substringAfter("base64,", "")
            if (base64Str.isEmpty()) return null

            val imageBytes = Base64.getDecoder().decode(base64Str)
            val filename = "${server.ip}_${server.port}.png"
            val file = File(offlineServersDir, filename)
            file.writeBytes(imageBytes)
            filename
        } catch (e: Exception) {
            LogConsole.error("Ошибка при сохранении favicon: ${e.message}")
            null
        }
    }

    fun appendOfflineServerToHtml(
        server: ServerInfo,
        primaryColorHex: String = "#6200EE",
        backgroundColorHex: String = "#FFFFFF",
        cardBackgroundColorHex: String = "#F9F9F9",
        textColorHex: String = "#000000"
    ) {
        try {
            val now = Date()
            val logTime = logDateFormat.format(now)
            val cleanedMotd = cleanMotdText(server.motd)
            val ipPort = "${server.ip}:${server.port}"
            val faviconFilename = saveFaviconToFile(server)
            val faviconHtml = faviconFilename?.let {
                """<img src="$it" alt="Favicon" style="width:32px;height:32px;vertical-align:middle;margin-right:8px;border:1px solid #ccc;"/>"""
            } ?: ""

            val serverBlock = """
            <div style="
                border:1px solid $primaryColorHex; 
                margin:8px; 
                padding:12px; 
                border-radius:6px; 
                background:$cardBackgroundColorHex; 
                font-family: Arial, sans-serif;
                color: $textColorHex;
                box-shadow: 2px 2px 6px rgba(0,0,0,0.1);
            ">
                <div style="font-weight:bold; font-size:1.2em; margin-bottom:6px; color: $primaryColorHex;">
                    $faviconHtml $ipPort
                </div>
                <div style="font-size:0.95em; margin-bottom:6px;">
                    <b>Версия:</b> ${server.version}<br>
                    <b>Игроки:</b> ${server.players.joinToString(", ").ifEmpty { "нет" }}<br>
                    <b>MOTD:</b> $cleanedMotd<br>
                    <b>Ping:</b> ${server.ping} мс<br>
                    <b>Зафиксировано:</b> $logTime
                </div>
            </div>
        """.trimIndent()

            if (!htmlFile.exists()) {
                val header = """
                <html><head><meta charset="UTF-8"><title>Offline servers - ${htmlFile.name}</title></head><body style="background-color:$backgroundColorHex; color:$textColorHex;">
                <h2 style="font-family: Arial, sans-serif; color: $primaryColorHex;">Оффлайн серверы, сохранено: ${logTime}</h2>
            """.trimIndent()
                htmlFile.writeText(header)
            }

            htmlFile.appendText(serverBlock)

            val content = htmlFile.readText()
            val contentWithoutClosing = content.removeSuffix("</body></html>")
            htmlFile.writeText("$contentWithoutClosing\n</body></html>")

            LogConsole.debug("Оффлайн сервер добавлен в ${htmlFile.absolutePath}")

        } catch (e: Exception) {
            LogConsole.error("Ошибка сохранения оффлайн сервера в HTML: ${e.message}")
        }
    }


    private fun loadProfiles(): List<Profile> {
        val loaded = ProfileStorage.loadProfiles()
        if (loaded.isEmpty()) {
            val defaultIps = listOf(
                IpPortsRange("51.75.167.121", 25500, 25700),
                IpPortsRange("148.251.56.99", 25560, 25700),
                IpPortsRange("135.181.49.9", 25500, 25700),
                IpPortsRange("88.198.26.90", 25500, 25700),
                IpPortsRange("51.161.201.179", 25560, 25700),
                IpPortsRange("139.99.71.89", 25500, 25700),
                IpPortsRange("135.148.104.247", 25500, 25700),
                IpPortsRange("51.81.251.246", 25500, 25700)
            )

            val defaultProfile = Profile(
                name = "Default",
                timeout = 500,
                parallels = Runtime.getRuntime().availableProcessors() * 50,
                targetIps = defaultIps,
                theme = AppTheme.MINECRAFT
            )

            saveProfiles(listOf(defaultProfile))
            return listOf(defaultProfile)
        }
        return loaded
    }

    fun startMonitoringFavorite(
        server: ServerInfo,
        colors: Colors
    ) {
        val desc = stripFormatting(server.motd.substringBefore("by")).trim()

        val primaryHex = colors.primary.toHex()
        val backgroundHex = colors.background.toHex()
        val cardBackgroundHex = colors.surface.toHex()
        val textHex = colors.onBackground.toHex()

        if (favoriteMonitoringJobs.containsKey(desc)) return

        val job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                val result = try {
                    scaneServer(
                        ip = server.ip,
                        port = server.port,
                        timeout = currentTimeout
                    )
                } catch (e: Exception) {
                    null
                }

                if (result == null) {
                    NotifierManager.show(
                        title = "Сервер выключен",
                        message = "⚠️ $desc (${server.ip}:${server.port}) больше не отвечает"
                    )
                    appendOfflineServerToHtml(server, primaryHex, backgroundHex, cardBackgroundHex, textHex)
                    break
                }

                delay(5000)
            }
        }

        favoriteMonitoringJobs[desc] = job
    }

    private fun stopMonitoringFavorite(desc: String) {
        favoriteMonitoringJobs[desc]?.cancel()
        favoriteMonitoringJobs.remove(desc)
    }

    fun renameCurrentProfile(newName: String) {
        val updated = profiles.toMutableList()
        if (currentProfileIndex in updated.indices) {
            val profile = updated[currentProfileIndex]
            updated[currentProfileIndex] = profile.copy(name = newName)
            profiles = updated
            saveProfiles(profiles)
        }
    }

    fun loadProfile(index: Int) {
        if (index !in profiles.indices) return

        LogConsole.debug("Загрузка профиля №$index")

        currentProfileIndex = index
        val profile = profiles[index]

        timeoutMs = profile.timeout
        parallelLimit = profile.parallels
        targetIps = profile.targetIps.toList()
        themeState.currentTheme = profile.theme
        currentTheme = profile.theme

        saveProfiles(profiles)
    }

    fun createNewProfile(name: String) {
        LogConsole.debug("Создан новый профиль")
        val defaultIps = listOf<IpPortsRange>()
        val defaultTimeout = 500
        val defaultParallels = 50

        val newProfile = Profile(
            name = name,
            timeout = defaultTimeout,
            parallels = defaultParallels,
            targetIps = defaultIps,
            theme = AppTheme.MINECRAFT
        )
        profiles = profiles.toMutableList().apply { add(newProfile) }
        currentProfileIndex = profiles.lastIndex

        saveProfiles(profiles)
    }

    fun updateSettings(timeout: Int, parallel: Int, ips: List<IpPortsRange>) {
        currentTimeout = timeout
        currentParallelLimit = parallel
        targetIps = ips
        currentTheme = themeState.currentTheme

        val updatedProfiles = profiles.toMutableList()
        val current = updatedProfiles.getOrNull(currentProfileIndex)
        if (current != null) {
            updatedProfiles[currentProfileIndex] = current.copy(
                timeout = timeout,
                parallels = parallel,
                targetIps = ips,
                theme = currentTheme
            )
            profiles = updatedProfiles
        }

        LogConsole.debug("Профиль обновлён")

        DataPreferences.putInt("timeout", timeout)
        DataPreferences.putInt("parallels", parallel)
        DataPreferences.putString("theme", currentTheme.name)

        val serialized = ips.joinToString("\n") { "${it.ip}:${it.portsStart}..${it.portsEnd}" }

        DataPreferences.putString("ips", serialized)

        saveProfiles(profiles)
    }

    fun startScan(scope: CoroutineScope) {
        scanning = true
        stopScan = false
        currentAction = "Сканируем..."
        scanProgress = 0f
        servers = listOf()

        scope.launch {
            val onlineServers = scanServers(
                ips = targetIps,
                timeout = currentTimeout,
                parallelLimit = currentParallelLimit,
                stopCondition = { stopScan },
                onProgress = { percent, text ->
                    scanProgress = percent
                    currentAction = text
                }
            )

            servers = onlineServers
            players = onlineServers.flatMap { it.players }.distinct()
            currentAction = if (stopScan)
                "Сканирование остановлено. Найдено: ${servers.size} серверов."
            else
                "Сканирование завершено. Найдено: ${servers.size} серверов."
            scanning = false
            stopScan = false
        }
    }

    fun stopScan() {
        stopScan = true
        currentAction = "Остановка..."
    }
}