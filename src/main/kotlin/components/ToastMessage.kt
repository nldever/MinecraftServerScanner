package components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun ToastMessage(
    message: String,
    durationMillis: Long = 2500,
    onDismiss: () -> Unit
) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(message) {
        delay(durationMillis)
        visible = false
        delay(300) // подождать анимацию исчезновения
        onDismiss()
    }

    val elevation = 8.dp
    val cornerRadius = 12.dp

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .wrapContentHeight()
                .shadow(elevation, RoundedCornerShape(cornerRadius))
                .background(
                    color = Color(0xDD323232),
                    shape = RoundedCornerShape(cornerRadius)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                color = Color.White,
                style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}