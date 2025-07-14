package components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dokar.sonner.Toast
import com.dokar.sonner.ToastType
import com.dokar.sonner.ToasterState
import decodeFavicon
import getTagsFromMotd
import kotlinx.coroutines.delay
import managers.ServerMonitoringManager
import models.ServerInfo
import models.view.MainViewModel
import parseMinecraftColoredJson

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MonitoringServerList(vm: MainViewModel, toaster: ToasterState, serversToShow: List<ServerInfo>) {
    val isDarkTheme = !MaterialTheme.colors.isLight

    val filteredServers = serversToShow.filter { server ->
        val text = vm.filterText.trim().lowercase()
        val matchesGeneral = text.isBlank() || listOf(
            server.ip,
            server.version,
            server.country,
            server.serverType,
            server.motd,
        ).any { it.lowercase().contains(text) } || server.players.any { it.lowercase().contains(text) }

        matchesGeneral &&
                (vm.filterVersion.isBlank() || server.version == vm.filterVersion) &&
                (vm.filterCountry.isBlank() || server.country == vm.filterCountry) &&
                (vm.filterServerType.isBlank() || server.serverType == vm.filterServerType)
    }

    val visibleServers = remember { mutableStateListOf<String>() }
    val expandedServerMap = remember { mutableStateMapOf<String, Boolean>() }

    LaunchedEffect(filteredServers) {
        visibleServers.clear()
        filteredServers.forEachIndexed { index, server ->
            delay(if (index < 7) 40L * index else 40L * 6)
            visibleServers.add("${server.ip}:${server.port}")
        }
    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        filteredServers.forEach { server ->
            val serverId = "${server.ip}:${server.port}"

            val monitoring = ServerMonitoringManager.monitoring.find {
                it.server.ip == server.ip && it.server.port == server.port
            }

            val lastHistory = monitoring?.history?.lastOrNull()

            val isChanged = lastHistory != null &&
                    (lastHistory.version != server.version ||
                            lastHistory.serverType != server.serverType ||
                            lastHistory.motd != server.motd)

            val showCurrent = expandedServerMap[serverId] ?: true

            AnimatedVisibility(
                visible = visibleServers.contains(serverId),
                enter = fadeIn(animationSpec = tween(300)) +
                        slideInVertically(animationSpec = tween(300), initialOffsetY = { it / 3 })
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colors.surface.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)
                    ) {
                        if (isChanged) {
                            // Старая инфа
                            ServerInfoBlock(
                                server = lastHistory.copy(isOnline = false),
                                isDarkTheme = isDarkTheme,
                                toaster = toaster,
                                vm,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .padding(end = 4.dp),
                            )

                            // Стрелочка
                            IconButton(onClick = {
                                expandedServerMap[serverId] = !showCurrent
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowRight,
                                    contentDescription = "Switch info"
                                )
                            }

                            // Новая инфа
                            ServerInfoBlock(
                                server = server,
                                isDarkTheme = isDarkTheme,
                                toaster = toaster,
                                vm,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .padding(start = 4.dp),
                            )
                        } else {
                            ServerInfoBlock(
                                server = server,
                                isDarkTheme = isDarkTheme,
                                toaster = toaster,
                                vm,
                                modifier = Modifier.weight(1f).fillMaxHeight(),
                            )
                        }

                        // ❤ Кнопка лайка
                        val isMon = vm.isMonitoring(server)
                        val scale = remember { Animatable(1f) }

                        LaunchedEffect(isMon) {
                            if (isMon) {
                                // Анимация добавления
                                scale.animateTo(1.3f, animationSpec = tween(150))
                                scale.animateTo(1f, animationSpec = tween(200))
                            } else {
                                // Анимация удаления
                                scale.animateTo(0.8f, animationSpec = tween(150))
                                scale.animateTo(1f, animationSpec = tween(200))
                            }
                        }

                        IconButton(
                            onClick = {
                                if (isMon) {
                                    vm.removeMonitoring(server)
                                    toaster.show(
                                        Toast("Сервер удалён из избранных", ToastType.Error)
                                    )
                                } else {
                                    vm.addMonitoring(server)
                                    toaster.show(
                                        Toast("Сервер добавлен в избранные", ToastType.Success)
                                    )
                                }
                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .graphicsLayer {
                                    scaleX = scale.value
                                    scaleY = scale.value
                                }
                        ) {
                            Icon(
                                imageVector = if (isMon) Icons.Filled.Visibility else Icons.Outlined.VisibilityOff,
                                contentDescription = if (isMon) "В избранных" else "Добавить в избранное",
                                tint = if (isMon) MaterialTheme.colors.primary
                                else MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }

            Divider(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
fun ServerInfoBlock(
    server: ServerInfo,
    isDarkTheme: Boolean,
    toaster: ToasterState,
    vm: MainViewModel,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colors.surface.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    vm.isMapDialogVisible = true
                }
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .clickable(true) {
                        clipboardManager.setText(AnnotatedString("${server.ip}:${server.port}"))
                        toaster.show(
                            Toast(
                                message = "ip скопирован",
                                type = ToastType.Success
                            )
                        )
                    }
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // 0. Иконка
                Column(modifier = Modifier.padding(12.dp)) {
                    decodeFavicon(server.favicon)?.let { bitmap ->
                        Image(
                            bitmap = bitmap,
                            contentDescription = "Server Icon",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(60.dp)
                        )
                    } ?: Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "No Icon",
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                        modifier = Modifier.size(48.dp)
                    )
                }

                // 1. Информация
                Column(
                    modifier = Modifier
                        .weight(2f)
                        .padding(10.dp)
                ) {
                    Row {
                        Text(
                            text = "IP: ${server.ip}:${server.port}",
                            style = MaterialTheme.typography.subtitle1,
                            color = MaterialTheme.colors.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.padding(8.dp))
                        StatusLabel(server.isOnline)
                    }


                    Text(
                        text = "Версия: ${server.version}, ${server.serverType}",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )

                    Text(
                        text = "Страна: ${server.country}, пинг: ${server.ping}мс",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )

                    Text(
                        text = parseMinecraftColoredJson(server.motd, isDarkTheme),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (server.players.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colors.primary.copy(alpha = 0.08f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Игроки:",
                                style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colors.onSurface
                            )

                            Text(
                                text = server.players.joinToString(", "),
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                // 2. Теги
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val tags = getTagsFromMotd(server.motd).toMutableList()
                    if (server.ping < 60) tags.add("Низкий пинг")

                    tags.forEach { tag ->
                        val isLowPing = tag == "Низкий пинг"
                        TagChip(
                            text = tag,
                            color = if (isLowPing) MaterialTheme.colors.secondary.copy(alpha = 0.3f)
                            else MaterialTheme.colors.primary.copy(alpha = 0.5f),
                            contentColor = if (isLowPing) MaterialTheme.colors.onSecondary
                            else MaterialTheme.colors.onPrimary,
                        )
                    }
                }
            }
        }
    }
}