package components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 6.dp,
    backgroundColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
    progressColor: Color = MaterialTheme.colors.primary
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 400),
        label = "Animated Progress"
    )

    Canvas(
        modifier = modifier
            .height(height)
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
    ) {
        val barWidth = size.width
        val barHeight = size.height

        drawRoundRect(
            color = backgroundColor,
            size = Size(barWidth, barHeight),
            cornerRadius = CornerRadius(barHeight / 2, barHeight / 2)
        )

        drawRoundRect(
            color = progressColor,
            size = Size(barWidth * animatedProgress, barHeight),
            cornerRadius = CornerRadius(barHeight / 2, barHeight / 2)
        )
    }
}