package components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import decodeFavicon
import extractAndJoinBeforeBy
import getTagsFromMotd
import managers.FavoriteStorage
import managers.ToastManager
import models.view.MainViewModel
import parseMinecraftColoredJson
import stripFormatting

@Composable
fun ServerList(vm: MainViewModel) {
    val toastManager = ToastManager.defaultManager
    val isDarkTheme = !MaterialTheme.colors.isLight
    val clipboardManager = LocalClipboardManager.current
    val filteredServers = vm.servers.filter { server ->
        val text = vm.filterText.trim().lowercase()

        val matchesGeneral = text.isBlank() || listOf(
            server.ip,
            server.version,
            server.country,
            server.serverType,
            server.motd,
        ).any { it.lowercase().contains(text) } ||
                server.players.any { it.lowercase().contains(text) }

        matchesGeneral &&
                (vm.filterVersion.isBlank() || server.version == vm.filterVersion) &&
                (vm.filterCountry.isBlank() || server.country == vm.filterCountry) &&
                (vm.filterServerType.isBlank() || server.serverType == vm.filterServerType)
    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        filteredServers.forEach { server ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colors.surface.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        vm.isMapDialogVisible = true
                        vm.mapLink = extractAndJoinBeforeBy(parseMinecraftColoredJson(server.motd, isDarkTheme)).lowercase()
                    }
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clickable(true) {
                            clipboardManager.setText(AnnotatedString("${server.ip}:${server.port}"))
                            toastManager.showToast("IP скопирован")
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

                    // 1. Инфа о сервере
                    Column(
                        modifier = Modifier
                            .weight(2f)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "IP: ${server.ip}:${server.port}",
                            style = MaterialTheme.typography.subtitle1,
                            color = MaterialTheme.colors.onSurface,
                            fontWeight = FontWeight.Bold
                        )

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

                    // 2. Чипы
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

                    // 3. Кнопки
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        val desc = stripFormatting(server.motd.substringBefore("by")).trim()
                        IconButton(
                            onClick = {
                                if (vm.isFavorite(desc)) {
                                    vm.removeFavorite(desc)
                                    toastManager.showToast("Удалено из избранных")
                                } else {
                                    vm.addFavorite(desc)
                                    toastManager.showToast("Добавлено в избранные")
                                }
                            },
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            val isFav = vm.isFavorite(desc)
                            Icon(
                                imageVector = if (isFav) Icons.Filled.Favorite else Icons.Outlined.Favorite,
                                contentDescription = if (isFav) "В избранных" else "Не в избранных",
                                tint = if (isFav) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                            )
                        }
                    }
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