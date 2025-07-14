package components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dokar.sonner.ToasterState
import models.TabPage
import models.view.MainViewModel

@Composable
fun AppWithTabs(vm: MainViewModel, toaster: ToasterState) {
    var selectedTab by remember { mutableStateOf(TabPage.Scanner) }

    val displayedServers = when (selectedTab) {
        TabPage.Scanner -> vm.servers
        TabPage.Favorites -> vm.favorites
        else -> emptyList()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab.ordinal) {
            TabPage.entries.forEachIndexed { index, page ->
                Tab(
                    selected = selectedTab.ordinal == index,
                    onClick = { selectedTab = page },
                    text = {
                        Row {
                            Icon(
                                imageVector = page.icon,
                                contentDescription = "Tab"
                            )
                            Text(page.name)
                        }
                    }
                )
            }
        }

        Divider(modifier = Modifier.height(1.dp))

        ServerList(vm, toaster, serversToShow = displayedServers)
    }
}
