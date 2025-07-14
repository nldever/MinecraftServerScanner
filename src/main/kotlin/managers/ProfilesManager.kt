package managers


import DataPreferences
import LogConsole
import models.view.MainViewModel
import models.IpPortsRange
import models.Profile
import themes.AppTheme

class ProfilesManager(private val vm: MainViewModel) {
    fun loadProfiles(): List<Profile> {
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
            ProfileStorage.saveProfiles(listOf(defaultProfile))
            return listOf(defaultProfile)
        }
        return loaded
    }

    fun loadProfile(index: Int) {
        if (index !in vm.profiles.indices) return

        LogConsole.debug("Загрузка профиля №$index")

        vm.currentProfileIndex = index
        val profile = vm.profiles[index]

        vm.timeoutMs = profile.timeout
        vm.parallelLimit = profile.parallels
        vm.targetIps = profile.targetIps.toList()
        vm.themeState.currentTheme = profile.theme
        vm.currentTheme = profile.theme

        ProfileStorage.saveProfiles(vm.profiles)
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
        vm.profiles = vm.profiles.toMutableList().apply { add(newProfile) }
        vm.currentProfileIndex = vm.profiles.lastIndex

        ProfileStorage.saveProfiles(vm.profiles)
    }

    fun renameCurrentProfile(newName: String) {
        val updated = vm.profiles.toMutableList()
        if (vm.currentProfileIndex in updated.indices) {
            val profile = updated[vm.currentProfileIndex]
            updated[vm.currentProfileIndex] = profile.copy(name = newName)
            vm.profiles = updated
            ProfileStorage.saveProfiles(vm.profiles)
        }
    }

    fun updateSettings(timeout: Int, parallel: Int, ips: List<IpPortsRange>) {
        vm.currentTimeout = timeout
        vm.currentParallelLimit = parallel
        vm.targetIps = ips
        vm.currentTheme = vm.themeState.currentTheme

        val updatedProfiles = vm.profiles.toMutableList()
        val current = updatedProfiles.getOrNull(vm.currentProfileIndex)
        if (current != null) {
            updatedProfiles[vm.currentProfileIndex] = current.copy(
                timeout = timeout,
                parallels = parallel,
                targetIps = ips,
                theme = vm.currentTheme
            )
            vm.profiles = updatedProfiles
        }

        LogConsole.debug("Профиль обновлён")

        // Сохраняем настройки в DataPreferences, если есть такая логика
        DataPreferences.putInt("timeout", timeout)
        DataPreferences.putInt("parallels", parallel)
        DataPreferences.putString("theme", vm.currentTheme.name)

        val serialized = ips.joinToString("\n") { "${it.ip}:${it.portsStart}..${it.portsEnd}" }
        DataPreferences.putString("ips", serialized)

        ProfileStorage.saveProfiles(vm.profiles)
    }
}