package managers

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import models.Profile
import java.io.File

object ProfileStorage {
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    // Файл, куда сохраняются профили в %APPDATA% или ~/.config
    val profilesFile: File
        get() {
            val appDataDir = when {
                System.getProperty("os.name").startsWith("Windows") ->
                    System.getenv("APPDATA") ?: System.getProperty("user.home")

                else ->
                    System.getenv("XDG_CONFIG_HOME") ?: "${System.getProperty("user.home")}/.config"
            }
            val folder = File(appDataDir, "MinecraftServerScanner")
            folder.mkdirs()
            return File(folder, "profiles.json")
        }

    fun saveProfiles(profiles: List<Profile>) {
        val jsonString = json.encodeToString(ListSerializer(Profile.serializer()), profiles)
        profilesFile.writeText(jsonString)
    }

    fun loadProfiles(): List<Profile> {
        if (!profilesFile.exists()) return emptyList()
        return try {
            val jsonString = profilesFile.readText()
            json.decodeFromString(ListSerializer(Profile.serializer()), jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
