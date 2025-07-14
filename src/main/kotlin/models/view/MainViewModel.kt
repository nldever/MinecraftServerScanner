import androidx.compose.material.Colors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import managers.FavoritesManager
import managers.OfflineStorage
import managers.ProfilesManager
import managers.ScanManager
import models.IpPortsRange
import models.Profile
import models.ServerInfo
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
    var profiles by mutableStateOf(listOf<Profile>())
    var currentProfileIndex by mutableStateOf(0)
    var targetIps by mutableStateOf(emptyList<IpPortsRange>())
    var currentTimeout by mutableStateOf(timeoutMs)
    var currentParallelLimit by mutableStateOf(parallelLimit)
    var currentTheme by mutableStateOf(AppTheme.MINECRAFT)

    // Favorites management
    private val favoritesManager = FavoritesManager(this)
    val favorites get() = favoritesManager.favorites

    // Profiles management
    private val profilesManager = ProfilesManager(this)

    // Offline storage
    private val offlineStorage = OfflineStorage()

    // Scan manager
    private val scanManager = ScanManager(this)

    init {
        profiles = profilesManager.loadProfiles()
        if (profiles.isNotEmpty()) {
            loadProfile(0)
        }
    }

    // Делегируем методы для избранного:
    fun addFavorite(server: ServerInfo) = favoritesManager.addFavorite(server)
    fun removeFavorite(server: ServerInfo) = favoritesManager.removeFavorite(server)
    fun isFavorite(server: ServerInfo) = favoritesManager.isFavorite(server)

    // Профили
    fun loadProfile(index: Int) = profilesManager.loadProfile(index)
    fun createNewProfile(name: String) = profilesManager.createNewProfile(name)
    fun renameCurrentProfile(newName: String) = profilesManager.renameCurrentProfile(newName)
    fun updateSettings(timeout: Int, parallel: Int, ips: List<IpPortsRange>) =
        profilesManager.updateSettings(timeout, parallel, ips)

    fun saveFaviconToFile(server: ServerInfo) = offlineStorage.saveFaviconToFile(server)

    // Сканирование
    fun startScan(scope: CoroutineScope) = scanManager.startScan(scope)
    fun stopScan() = scanManager.stopScan()
}