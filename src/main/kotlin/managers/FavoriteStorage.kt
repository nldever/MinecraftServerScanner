package managers

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import models.ServerInfo
import stripFormatting
import java.io.File

object FavoriteStorage {
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    val favoritesFile: File
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

    fun saveFavorites(favorites: List<String>) {
        val jsonString = json.encodeToString(ListSerializer(String.serializer()), favorites)
        favoritesFile.writeText(jsonString)
    }

    fun loadFavorites(): List<String> {
        if (!favoritesFile.exists()) return emptyList()
        return try {
            val jsonString = favoritesFile.readText()
            json.decodeFromString(ListSerializer(String.serializer()), jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

}