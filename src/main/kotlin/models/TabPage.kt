package models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.ui.graphics.vector.ImageVector

enum class TabPage(val icon: ImageVector, val title: String) {
    Scanner(Icons.Default.Search, "Сканер"),
    Monitoring(Icons.Default.Visibility, "Мониторинг"),
    Players(Icons.Default.Person2, "Игроки"),
    Settings(Icons.Default.Settings, "Настройки"),
}