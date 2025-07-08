package components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import themes.AppTheme

@Composable
fun ThemeDropdown(
    themes: List<AppTheme>,
    selectedTheme: AppTheme,
    defaultText: String = "Выберите тему",
    onThemeSelected: (AppTheme) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val textColor = if (selectedTheme == AppTheme.MINECRAFT)
        MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
    else
        MaterialTheme.colors.onSurface

    val borderColor = if (expanded)
        MaterialTheme.colors.primary
    else
        MaterialTheme.colors.onSurface.copy(alpha = 0.2f)

    Box(
        modifier = modifier
            .border(BorderStroke(1.dp, borderColor), shape = RoundedCornerShape(4.dp))
            .clickable { expanded = true }
            .padding(horizontal = 12.dp, vertical = 14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = if (selectedTheme == AppTheme.MINECRAFT) defaultText else selectedTheme.name.lowercase()
                    .replaceFirstChar { it.uppercase() },
                color = textColor,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(onClick = {
                onThemeSelected(AppTheme.MINECRAFT)
                expanded = false
            }) {
                Text("Очистить", color = MaterialTheme.colors.onSurface)
            }
            themes.forEach { theme ->
                DropdownMenuItem(onClick = {
                    onThemeSelected(theme)
                    expanded = false
                }) {
                    Text(
                        theme.name.lowercase().replaceFirstChar { it.uppercase() },
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    }
}