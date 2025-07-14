package screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dokar.sonner.ToasterState
import components.*
import models.TabPage
import models.view.MainViewModel
import themes.ThemeState

@Composable
fun MainScreen(themeState: ThemeState, toaster: ToasterState, vm: MainViewModel) {
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableStateOf(TabPage.Scanner) }

    val displayedServers = when (selectedTab) {
        TabPage.Scanner -> vm.servers
        TabPage.Monitoring -> vm.monitorings
        else -> emptyList()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Панель сканирования
        ScanControls(vm, scope)
        Spacer(modifier = Modifier.height(12.dp))

        // Вкладки
        ScrollableTabRow(
            selectedTabIndex = selectedTab.ordinal,
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.primary,
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                    color = MaterialTheme.colors.primary
                )
            }
        ) {
            TabPage.entries.forEachIndexed { index, page ->
                val selected = selectedTab.ordinal == index
                Tab(
                    selected = selected,
                    onClick = { selectedTab = page },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = page.icon,
                                contentDescription = "${page.name} Icon",
                                modifier = Modifier.size(20.dp),
                                tint = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                            )
                            Text(
                                text = page.title,
                                color = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Фильтры
        if(selectedTab.name != "Settings") {
            FilterRow(vm)
            Spacer(modifier = Modifier.height(12.dp))
        }

        Divider(modifier = Modifier.height(1.dp))

        // Сервера
        when (selectedTab) {
            TabPage.Scanner -> ScannerServerList(vm, toaster)
            TabPage.Monitoring -> MonitoringServerList(vm, toaster, vm.monitorings)
            TabPage.Players -> PlayersList(vm, toaster)
            TabPage.Settings -> SettingsScreen(
                vm = vm,
                themeState = themeState,
                profiles = vm.profiles,
                currentProfileIndex = vm.currentProfileIndex,
                onProfileSelected = vm::loadProfile,
                onNewProfile = { vm.createNewProfile("new profile ${vm.profiles.size + 1}") },
            )

            else -> null
        }
    }
}