package models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class TabPage(val icon: ImageVector, val title: String) {
    Scanner(Icons.Default.Search, "Сканер"),
    Favorites(Icons.Default.Favorite,"Избранное"),
    Players(Icons.Default.Person,"Игроки")
}