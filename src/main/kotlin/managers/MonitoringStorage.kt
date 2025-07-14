package managers

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import models.ServerInfo
import java.io.File

object MonitoringStorage {
    val file: File
        get() {
            val appDataDir = when {
                System.getProperty("os.name").startsWith("Windows") ->
                    System.getenv("APPDATA") ?: System.getProperty("user.home")

                else ->
                    System.getenv("XDG_CONFIG_HOME") ?: "${System.getProperty("user.home")}/.config"
            }
            val folder = File(appDataDir, "MinecraftServerScanner")
            folder.mkdirs()
            return File(folder, "Monitorings.json")
        }

    fun saveMonitoring(Monitorings: List<ServerInfo>) {
        val json = Json.encodeToString(ListSerializer(ServerInfo.serializer()), Monitorings)
        file.writeText(json)
    }

    fun loadMonitoring(): List<ServerInfo> {
        if (!file.exists()) return emptyList()
        val json = file.readText()
        return Json.decodeFromString(json)
    }
}