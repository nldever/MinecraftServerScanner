package components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import models.view.MainViewModel

@Composable
fun FilterRow(vm: MainViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StyledPlayerTextField(
            playerName = vm.filterText,
            placeholder = "Поиск",
            onPlayerNameChange = { vm.filterText = it },
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
        )

        VersionDropdown(
            versions = vm.servers.map { it.version }.distinct().sorted(),
            selectedVersion = vm.filterVersion,
            defaultText = "Версия",
            onVersionSelected = { vm.filterVersion = it },
            modifier = Modifier.weight(1f).height(50.dp)
        )

        VersionDropdown(
            versions = vm.servers.map { it.country }.distinct().sorted(),
            selectedVersion = vm.filterCountry,
            defaultText = "Страна",
            onVersionSelected = { vm.filterCountry = it },
            modifier = Modifier.weight(1f).height(50.dp)
        )

        VersionDropdown(
            versions = vm.servers.map { it.serverType }.distinct().sorted(),
            selectedVersion = vm.filterServerType,
            defaultText = "Тип",
            onVersionSelected = { vm.filterServerType = it },
            modifier = Modifier.weight(1f).height(50.dp)
        )
    }
}