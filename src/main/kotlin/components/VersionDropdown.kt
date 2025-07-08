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

@Composable
fun VersionDropdown(
    versions: List<String>,
    selectedVersion: String,
    defaultText: String,
    onVersionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val textColor = if (selectedVersion.isBlank())
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
                text = selectedVersion.ifBlank { defaultText },
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
                onVersionSelected("")
                expanded = false
            }) {
                Text("Очистить", color = MaterialTheme.colors.onSurface)
            }
            versions.forEach { version ->
                DropdownMenuItem(onClick = {
                    onVersionSelected(version)
                    expanded = false
                }) {
                    Text(version, color = MaterialTheme.colors.onSurface)
                }
            }
        }
    }
}