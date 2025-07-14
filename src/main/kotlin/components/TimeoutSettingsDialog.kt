package components

import models.view.MainViewModel
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import models.Profile
import parseIpPorts
import themes.AppTheme
import themes.ThemeState

@Composable
fun TimeoutSettingsDialog(
    vm: MainViewModel,
    themeState: ThemeState,
    profiles: List<Profile>,
    currentProfileIndex: Int,
    onProfileSelected: (Int) -> Unit,
    onNewProfile: () -> Unit,
    onDismiss: () -> Unit
) {
    rememberScrollState()
    var ipText by remember(currentProfileIndex) {
        mutableStateOf(
            vm.targetIps.joinToString("\n") {
                "${it.ip}:${it.portsStart}..${it.portsEnd}"
            }
        )
    }
    var profileName by remember(currentProfileIndex) {
        mutableStateOf(profiles.getOrNull(currentProfileIndex)?.name ?: "")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxHeight(0.95f), // чтобы не выходило за экран
        title = { Text("Настройки", style = MaterialTheme.typography.h6) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Профили
                Text("Профиль", fontWeight = FontWeight.Bold)
                var expanded by remember { mutableStateOf(false) }
                Box {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true }
                            .border(
                                1.dp,
                                MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            profiles.getOrNull(currentProfileIndex)?.name ?: "Не выбран",
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    }

                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        profiles.forEachIndexed { index, profile ->
                            DropdownMenuItem(onClick = {
                                onProfileSelected(index)
                                expanded = false
                            }) {
                                Text(profile.name)
                            }
                        }
                        Divider()
                        DropdownMenuItem(onClick = {
                            onNewProfile()
                            expanded = false
                        }) {
                            Text("+ Новый профиль")
                        }
                    }
                }

                OutlinedTextField(
                    value = profileName,
                    onValueChange = {
                        profileName = it
                        vm.renameCurrentProfile(it)
                    },
                    label = { Text("Имя профиля") },
                    modifier = Modifier.fillMaxWidth()
                )

                Divider()

                Text("Тема оформления", fontWeight = FontWeight.Bold)
                ThemeDropdown(
                    themes = AppTheme.entries,
                    selectedTheme = themeState.currentTheme,
                    defaultText = "Выберите тему",
                    onThemeSelected = { themeState.currentTheme = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Divider()

                Text("Сканирование", fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = vm.timeoutMs.toString(),
                    onValueChange = { vm.timeoutMs = it.toIntOrNull() ?: vm.timeoutMs },
                    label = { Text("Таймаут (мс)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = vm.parallelLimit.toString(),
                    onValueChange = { vm.parallelLimit = it.toIntOrNull() ?: vm.parallelLimit },
                    label = { Text("Количество потоков") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = ipText,
                    onValueChange = { ipText = it },
                    label = { Text("IP и порты (пример: 1.2.3.4:100..140)") },
                    maxLines = 10,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsed = parseIpPorts(ipText)
                    vm.updateSettings(vm.timeoutMs, vm.parallelLimit, parsed)
                    onDismiss()
                }
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
