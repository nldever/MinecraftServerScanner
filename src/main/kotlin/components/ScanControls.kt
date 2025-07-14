package components

import LogConsole
import models.view.MainViewModel
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ScanControls(vm: MainViewModel, scope: CoroutineScope) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (vm.scanning) {
            Button(
                onClick = { vm.stopScan() },
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text("Стоп", color = Color.White)
            }
        } else {
            Button(onClick = { vm.startScan(scope) }) {
                Text("Сканировать сервера")
            }
        }

        Column(
            modifier = Modifier
                .padding(12.dp)
                .clickable(true) {
                    LogConsole.show()
                }
                .weight(1f)
        ) {
            Text(vm.currentAction, style = MaterialTheme.typography.subtitle1)

            AnimatedVisibility(
                visible = vm.scanning,
                enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
                    initialOffsetY = { -10 },
                    animationSpec = tween(300)
                ),
                exit = fadeOut(animationSpec = tween(200)) + slideOutVertically(
                    targetOffsetY = { -10 },
                    animationSpec = tween(200)
                )
            ) {
                AnimatedProgressBar(
                    progress = vm.scanProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }
        }

        IconButton(onClick = { vm.showSettings = true }) {
            Icon(Icons.Default.Settings, contentDescription = "Настройки")
        }
    }
}