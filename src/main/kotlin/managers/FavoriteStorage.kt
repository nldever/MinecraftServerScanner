package managers

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import models.Profile
import models.ServerInfo
import java.io.File

object FavoriteStorage {
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
            return File(folder, "favorites.json")
        }

    fun saveFavorites(favorites: List<ServerInfo>) {
        val json = Json.encodeToString(ListSerializer(ServerInfo.serializer()), favorites)
        file.writeText(json)
    }

    fun loadFavorites(): List<ServerInfo> {
        if (!file.exists()) return emptyList()
        val json = file.readText()
        return Json.decodeFromString(json)
    }
}