import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.dokar.sonner.Toaster
import com.dokar.sonner.rememberToasterState
import managers.MonitoringsManager
import managers.ProfileStorage
import models.IpPortsRange
import models.Profile
import models.view.MainViewModel
import screens.MainScreen
import themes.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App(themeState: ThemeState) {
    val vm = remember { MainViewModel(themeState) }
    val scope = rememberCoroutineScope()
    val toaster = rememberToasterState()
    val MonitoringsManager = MonitoringsManager(vm)

    var showContent by remember { mutableStateOf(false) }

    MonitoringsManager.startMonitoring()

    LaunchedEffect(Unit) {

        showContent = true

    }

    MaterialTheme(
        colors = getThemeColors(themeState.currentTheme),
        typography = getThemeTypography(themeState.currentTheme),
        shapes = getThemeShapes(themeState.currentTheme)
    ) {

        Toaster(
            state = toaster,
            showCloseButton = true,
            darkTheme = MaterialTheme.colors.isLight,
            richColors = true,
            alignment = Alignment.BottomCenter
        )


        Surface(modifier = Modifier.fillMaxSize()) {
            Box {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(1000))
                ) {
                    themeState.currentTheme.backgroundImagePath?.let { path ->
                        Image(
                            painter = painterResource(path),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } ?: Box(
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colors.background)
                    )
                }

                AnimatedVisibility(
                    visible = showContent,
                    enter = fadeIn(animationSpec = tween(500)) + slideInVertically(initialOffsetY = { it / 2 }),
                ) {
                    Column(Modifier.padding(16.dp)) {
                        MainScreen(themeState, toaster, vm)
                    }
                }

//                if (vm.showSettings) {
//                    TimeoutSettingsDialog(
//                        vm = vm,
//                        themeState = themeState,
//                        profiles = vm.profiles,
//                        currentProfileIndex = vm.currentProfileIndex,
//                        onProfileSelected = vm::loadProfile,
//                        onNewProfile = { vm.createNewProfile("new profile ${vm.profiles.size + 1}") },
//                        onDismiss = { vm.showSettings = false }
//                    )
//                }
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

    val icon: Painter = painterResource("icon.png")

    Window(
        onCloseRequest = ::exitApplication,
        title = "Сканер серверов Minecraft",
        icon = icon
    ) {
        App(themeState)
    }
}