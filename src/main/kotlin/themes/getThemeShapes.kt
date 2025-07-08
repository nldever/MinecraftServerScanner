package themes

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import components.Theme.minecraftShapes

@Composable
fun getThemeShapes(theme: AppTheme) = when (theme) {
    AppTheme.LIGHT -> Shapes()
    AppTheme.DARK -> Shapes()
    AppTheme.MINECRAFT -> minecraftShapes

    AppTheme.RETRO_8BIT -> Shapes(
        small = RoundedCornerShape(0.dp),
        medium = RoundedCornerShape(0.dp),
        large = RoundedCornerShape(0.dp)
    )

    AppTheme.NEON_CYBERPUNK -> Shapes(
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(16.dp),
        large = RoundedCornerShape(0.dp)
    )

    AppTheme.PASTEL -> Shapes(
        small = RoundedCornerShape(12.dp),
        medium = RoundedCornerShape(24.dp),
        large = RoundedCornerShape(24.dp)
    )

    AppTheme.GRADIENT -> Shapes(
        small = RoundedCornerShape(10.dp),
        medium = RoundedCornerShape(18.dp),
        large = RoundedCornerShape(20.dp)
    )

    AppTheme.MONOCHROME -> Shapes()

    AppTheme.DARK_ACCENT -> Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(8.dp),
        large = RoundedCornerShape(0.dp)
    )

    AppTheme.NATURE -> Shapes(
        small = RoundedCornerShape(16.dp),
        medium = RoundedCornerShape(24.dp),
        large = RoundedCornerShape(24.dp)
    )

    AppTheme.RAINBOW -> Shapes()

    AppTheme.VINTAGE -> Shapes(
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(12.dp)
    )

    // Новые темы:
    AppTheme.PIXEL_ART -> Shapes(
        small = RoundedCornerShape(0.dp),
        medium = RoundedCornerShape(0.dp),
        large = RoundedCornerShape(0.dp)
    )

    AppTheme.FUTURISTIC_HUD -> Shapes(
        small = RoundedCornerShape(6.dp),
        medium = RoundedCornerShape(14.dp),
        large = RoundedCornerShape(4.dp)
    )

    AppTheme.MIDNIGHT_BLUE -> Shapes(
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(16.dp),
        large = RoundedCornerShape(16.dp)
    )

    AppTheme.WOODLAND -> Shapes(
        small = RoundedCornerShape(10.dp),
        medium = RoundedCornerShape(18.dp),
        large = RoundedCornerShape(20.dp)
    )

    AppTheme.OCEANIC -> Shapes(
        small = RoundedCornerShape(16.dp),
        medium = RoundedCornerShape(24.dp),
        large = RoundedCornerShape(24.dp)
    )

    AppTheme.DESERT_SUNSET -> Shapes(
        small = RoundedCornerShape(12.dp),
        medium = RoundedCornerShape(20.dp),
        large = RoundedCornerShape(28.dp)
    )

    AppTheme.TERMINAL_GREEN -> Shapes(
        small = RoundedCornerShape(0.dp),
        medium = RoundedCornerShape(2.dp),
        large = RoundedCornerShape(2.dp)
    )

    AppTheme.SOLARIZED_LIGHT -> Shapes(
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(12.dp)
    )

    AppTheme.SOLARIZED_DARK -> Shapes(
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(12.dp)
    )

    AppTheme.STARRY_NIGHT -> Shapes(
        small = RoundedCornerShape(10.dp),
        medium = RoundedCornerShape(18.dp),
        large = RoundedCornerShape(24.dp)
    )
}