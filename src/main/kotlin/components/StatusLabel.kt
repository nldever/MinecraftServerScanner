package components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StatusLabel(isOnline: Boolean) {
    val backgroundColor = if (isOnline) Color(0xFF4CAF50) else Color(0xFFF44336)

    val contentColor = if (isOnline) {
        MaterialTheme.colors.error
    } else {
        MaterialTheme.colors.secondary
    }

    val text = if (isOnline) "Online" else "Offline"

    Box(
        modifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.caption
        )
    }
}