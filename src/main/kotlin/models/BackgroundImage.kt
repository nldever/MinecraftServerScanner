package models

import LogConsole
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import models.view.MainViewModel

// ScanControls.kt
@Composable
fun ScanControls(vm: MainViewModel, scope: CoroutineScope) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (vm.scanning) {
            Button(onClick = { vm.stopScan() }, colors = ButtonDefaults.buttonColors(Color.Red)) {
                Text("Стоп", color = Color.White)
            }
        } else {
            Button(onClick = { vm.startScan(scope) }) {
                Text("Сканировать сервера")
            }
        }

        Column(
            Modifier
                .padding(12.dp)
                .clickable(true) {
                    LogConsole.show()
                }

        ) {
            Text(vm.currentAction, style = MaterialTheme.typography.subtitle1)
            if (vm.scanning) {
                LinearProgressIndicator(
                    progress = vm.scanProgress,
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(MaterialTheme.shapes.small)
                )
            }
        }

        IconButton(onClick = { vm.showSettings = true }) {
            Icon(Icons.Default.Settings, contentDescription = "Настройки")
        }
    }
}
