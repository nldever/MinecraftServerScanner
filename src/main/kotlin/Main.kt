import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import components.FilterRow
import components.ServerList
import components.TimeoutSettingsDialog
import components.ToastsStack
import managers.ProfileStorage
import managers.ToastManager
import models.IpPortsRange
import models.Profile
import models.ScanControls
import models.view.MainViewModel
import themes.*
import java.awt.Desktop
import java.net.URI

@Composable
fun App(themeState: ThemeState) {
    val vm = remember { MainViewModel(themeState) }
    val scope = rememberCoroutineScope()
    val toastManager = ToastManager.defaultManager
    MaterialTheme(
        colors = getThemeColors(themeState.currentTheme),
        typography = getThemeTypography(themeState.currentTheme),
        shapes = getThemeShapes(themeState.currentTheme)
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box {

                themeState.currentTheme.backgroundImagePath?.let { path ->
                    Image(
                        painter = painterResource(path),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } ?: Box(Modifier.fillMaxSize().background(MaterialTheme.colors.background))

                Column(Modifier.padding(16.dp)) {
                    ScanControls(vm, scope)
                    FilterRow(vm)
                    ServerList(vm)
                }
                if (vm.showSettings) {
                    TimeoutSettingsDialog(
                        vm = vm,
                        themeState = themeState,
                        profiles = vm.profiles,
                        currentProfileIndex = vm.currentProfileIndex,
                        onProfileSelected = vm::loadProfile,
                        onNewProfile = { vm.createNewProfile("new profile ${vm.profiles.size + 1}") },
                        onDismiss = { vm.showSettings = false }
                    )
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    ToastsStack(
                        toastManager = toastManager,
                        modifier = Modifier.align(Alignment.BottomEnd)
                    )
                }
            }
        }
    }
}


fun main() = application {
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

    // Загрузка или создание profiles.json
    if (!ProfileStorage.profilesFile.exists()) {
        val defaultProfile = Profile(
            name = "trial.stickypiston.co",
            timeout = 500,
            parallels = 50,
            theme = AppTheme.MINECRAFT,
            targetIps = defaultIps
        )
        ProfileStorage.saveProfiles(listOf(defaultProfile))
    }

    // Загружаем профили
    val profiles = ProfileStorage.loadProfiles()
    val initialProfile = profiles.getOrNull(0)

    val themeState = remember { ThemeState(initialProfile?.theme ?: AppTheme.MINECRAFT) }

    val icon: Painter = painterResource("MinecraftScannerIcon.png")

    Window(
        onCloseRequest = ::exitApplication,
        title = "Сканер серверов Minecraft",
        icon = icon
    ) {
        App(themeState)
    }
}


fun openInDefaultBrowser(url: String) {
    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
        try {
            Desktop.getDesktop().browse(URI(url))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    } else {
        println("Desktop browsing is not supported on this platform.")
    }
}