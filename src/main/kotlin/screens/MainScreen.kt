package screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dokar.sonner.ToasterState
import components.FilterRow
import components.ScanControls
import components.ServerList
import models.TabPage
import models.view.MainViewModel
import themes.ThemeState

@Composable
fun MainScreen(themeState: ThemeState, toaster: ToasterState, vm: MainViewModel) {
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableStateOf(TabPage.Scanner) }

    val displayedServers = when (selectedTab) {
        TabPage.Scanner -> vm.servers
        TabPage.Favorites -> vm.favorites
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
        FilterRow(vm)
        Spacer(modifier = Modifier.height(12.dp))

        Divider(modifier = Modifier.height(1.dp))

        // Сервера
        ServerList(vm, toaster, displayedServers)
    }
}