package models.view

import DataPreferences
import LogConsole
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import managers.ProfileStorage
import managers.ProfileStorage.saveProfiles
import models.IpPortsRange
import models.Profile
import models.ServerInfo
import scanServers
import themes.AppTheme
import themes.ThemeState

class MainViewModel(val themeState: ThemeState) {
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

    // Профили — список настроек
    var profiles by mutableStateOf(loadProfiles())

    // Индекс текущего активного профиля
    var currentProfileIndex by mutableStateOf(0)

    // Данные для сканирования, зависящие от выбранного профиля
    var targetIps by mutableStateOf(profiles.getOrNull(currentProfileIndex)?.targetIps ?: emptyList())
    var currentTimeout by mutableStateOf(profiles.getOrNull(currentProfileIndex)?.timeout ?: timeoutMs)
    var currentParallelLimit by mutableStateOf(profiles.getOrNull(currentProfileIndex)?.parallels ?: parallelLimit)
    var currentTheme by mutableStateOf(profiles.getOrNull(currentProfileIndex)?.theme ?: AppTheme.MINECRAFT)

    private fun loadProfiles(): List<Profile> {
        val loaded = ProfileStorage.loadProfiles()
        if (loaded.isEmpty()) {
            // Создаём дефолтный профиль
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
        val defaultIps = listOf<IpPortsRange>() // пустой список или дефолтный
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

        // Обновляем текущий профиль
        val updatedProfiles = profiles.toMutableList()
        val current = updatedProfiles.getOrNull(currentProfileIndex)
        if (current != null) {
            updatedProfiles[currentProfileIndex] = current.copy(
                timeout = timeout,
                parallels = parallel,
                targetIps = ips,
                theme = currentTheme  // очень важно!
            )
            profiles = updatedProfiles
        }

        LogConsole.debug("Профиль обновлён")

        // Обновляем DataPreferences
        DataPreferences.putInt("timeout", timeout)
        DataPreferences.putInt("parallels", parallel)
        DataPreferences.putString("theme", currentTheme.name) // если хочешь сохранить в DataPreferences

        val serialized = ips.joinToString("\n") { "${it.ip}:${it.portsStart}..${it.portsEnd}" }
        DataPreferences.putString("ips", serialized)

        // Сохраняем в файл профилей
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