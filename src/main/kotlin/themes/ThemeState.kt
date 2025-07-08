package themes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ThemeState(initialTheme: AppTheme = AppTheme.MINECRAFT) {
    var currentTheme by mutableStateOf(initialTheme)
}