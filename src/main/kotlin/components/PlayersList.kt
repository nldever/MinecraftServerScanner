package components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dokar.sonner.Toast
import com.dokar.sonner.ToastType
import com.dokar.sonner.ToasterState
import kotlinx.coroutines.delay
import models.view.MainViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayersList(vm: MainViewModel, toaster: ToasterState) {
    val isDarkTheme = !MaterialTheme.colors.isLight
    val clipboardManager = LocalClipboardManager.current

    // Собираем всех игроков из всех серверов в один список с инфой, с какого сервера они
    val allPlayers = remember(vm.servers) {
        vm.servers.flatMap { server ->
            server.players.map { playerName ->
                PlayerInfo(
                    name = playerName,
                    serverIp = server.ip,
                    serverPort = server.port,
                    serverVersion = server.version,
                    serverCountry = server.country,
                    serverPing = server.ping
                )
            }
        }
    }

    val filterText = vm.filterText.trim().lowercase()

    val filteredPlayers = allPlayers.filter { player ->
        filterText.isBlank() || player.name.lowercase().contains(filterText)
    }

    val visiblePlayers = remember { mutableStateListOf<String>() }
    LaunchedEffect(filteredPlayers) {
        visiblePlayers.clear()
        filteredPlayers.forEachIndexed { index, player ->
            delay(if (index < 7) 40L * index else 40L * 6)
            visiblePlayers.add(player.name + "@" + player.serverIp)
        }
    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        filteredPlayers.forEach { player ->
            val playerId = player.name + "@" + player.serverIp

            AnimatedVisibility(
                visible = visiblePlayers.contains(playerId),
                enter = fadeIn(animationSpec = tween(300)) +
                        slideInVertically(animationSpec = tween(300), initialOffsetY = { it / 3 })
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colors.surface.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable {
                                    clipboardManager.setText(AnnotatedString(player.name))
                                    toaster.show(
                                        Toast(
                                            message = "Ник скопирован",
                                            type = ToastType.Success
                                        )
                                    )
                                }
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PlayerFullBodySkinImage(player.name)

                            Column(
                                modifier = Modifier
                                    .weight(2f)
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = player.name,
                                    style = MaterialTheme.typography.subtitle1,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.onSurface
                                )
                                Text(
                                    text = "Сервер: ${player.serverIp}:${player.serverPort} (${player.serverVersion})",
                                    style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = "Страна сервера: ${player.serverCountry}, Пинг: ${player.serverPing} мс",
                                    style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                )
                            }

                            // Здесь можно добавить кнопки, если нужно

                        }
                    }

                    Divider(
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}

// Вспомогательный класс для плоского списка игроков с инфой о сервере
data class PlayerInfo(
    val name: String,
    val serverIp: String,
    val serverPort: Int,
    val serverVersion: String,
    val serverCountry: String,
    val serverPing: Int
)