package managers

import LogConsole
import cleanMotdText
import models.ServerInfo
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class OfflineStorage {
    private val appDataDir = when {
        System.getProperty("os.name").startsWith("Windows") ->
            System.getenv("APPDATA") ?: System.getProperty("user.home")

        else ->
            System.getenv("XDG_CONFIG_HOME") ?: "${System.getProperty("user.home")}/.config"
    }
    private val offlineServersDir = File(appDataDir, "MinecraftServerScanner")
    private val htmlFileDateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm")
    private val logDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")

    val htmlFile: File
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
}
