package themes

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import components.Theme.minecraftColors

@Composable
fun getThemeColors(theme: AppTheme) = when (theme) {
    AppTheme.LIGHT -> lightColors(
        primary = Color(0xFF6200EE),
        primaryVariant = Color(0xFF3700B3),
        secondary = Color(0xFF03DAC6),
        background = Color(0xFFFFFFFF),
        surface = Color(0xFFFFFFFF),
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black
    )

    AppTheme.DARK -> darkColors(
        primary = Color(0xFFBB86FC),
        primaryVariant = Color(0xFF3700B3),
        secondary = Color(0xFF03DAC6),
        background = Color(0xFF121212),
        surface = Color(0xFF121212),
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onBackground = Color.White,
        onSurface = Color.White
    )

    AppTheme.MINECRAFT -> minecraftColors

    AppTheme.RETRO_8BIT -> lightColors(
        primary = Color(0xFFE6194B),
        primaryVariant = Color(0xFF3CB44B),
        secondary = Color(0xFF4363D8),
        background = Color(0xFF000000),
        surface = Color(0xFF000000),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color.White,
        onSurface = Color.White
    )

    AppTheme.NEON_CYBERPUNK -> darkColors(
        primary = Color(0xFFFF6EC7),
        primaryVariant = Color(0xFF18FFFF),
        secondary = Color(0xFF39FF14),
        background = Color(0xFF0F0F0F),
        surface = Color(0xFF0F0F0F),
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onBackground = Color.White,
        onSurface = Color.White
    )

    AppTheme.PASTEL -> lightColors(
        primary = Color(0xFFFFB3BA),
        primaryVariant = Color(0xFFFFDAC1),
        secondary = Color(0xFFBAE1FF),
        background = Color(0xFFFFFFFF),
        surface = Color(0xFFFFFFFF),
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black
    )

    AppTheme.GRADIENT -> lightColors(
        primary = Color(0xFF3A1C71),
        primaryVariant = Color(0xFFD76D77),
        secondary = Color(0xFFFFAF7B),
        background = Color(0xFFFFFFFF),
        surface = Color(0xFFFFFFFF),
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black
    )

    AppTheme.MONOCHROME -> lightColors(
        primary = Color(0xFF333333),
        primaryVariant = Color(0xFF666666),
        secondary = Color(0xFF999999),
        background = Color(0xFFF0F0F0),
        surface = Color(0xFFFFFFFF),
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black
    )

    AppTheme.DARK_ACCENT -> darkColors(
        primary = Color(0xFFFF5722),
        primaryVariant = Color(0xFFD84315),
        secondary = Color(0xFFFFC107),
        background = Color(0xFF121212),
        surface = Color(0xFF121212),
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onBackground = Color.White,
        onSurface = Color.White
    )

    AppTheme.NATURE -> lightColors(
        primary = Color(0xFF4CAF50),
        primaryVariant = Color(0xFF2E7D32),
        secondary = Color(0xFF8D6E63),
        background = Color(0xFFF1F8E9),
        surface = Color(0xFFFFFFFF),
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black
    )

    AppTheme.RAINBOW -> lightColors(
        primary = Color(0xFFE91E63),
        primaryVariant = Color(0xFFFFC107),
        secondary = Color(0xFF2196F3),
        background = Color(0xFFFFFFFF),
        surface = Color(0xFFFFFFFF),
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black
    )

    AppTheme.VINTAGE -> lightColors(
        primary = Color(0xFFA1887F),
        primaryVariant = Color(0xFF6D4C41),
        secondary = Color(0xFFD7CCC8),
        background = Color(0xFFFFF8E1),
        surface = Color(0xFFFFF8E1),
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black
    )

    // Новые темы:
    AppTheme.PIXEL_ART -> lightColors(
        primary = Color(0xFFFF0000),
        primaryVariant = Color(0xFF00FF00),
        secondary = Color(0xFF0000FF),
        background = Color(0xFF101010),
        surface = Color(0xFF1A1A1A),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color.White,
        onSurface = Color.White
    )

    AppTheme.FUTURISTIC_HUD -> darkColors(
        primary = Color(0xFF00E5FF),
        primaryVariant = Color(0xFF00B8D4),
        secondary = Color(0xFF00C853),
        background = Color(0xFF000000),
        surface = Color(0xFF121212),
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onBackground = Color.White,
        onSurface = Color.White
    )

    AppTheme.MIDNIGHT_BLUE -> darkColors(
        primary = Color(0xFF003366),
        primaryVariant = Color(0xFF001F3F),
        secondary = Color(0xFF3399FF),
        background = Color(0xFF0D1B2A),
        surface = Color(0xFF1B263B),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color.White,
        onSurface = Color.White
    )

    AppTheme.WOODLAND -> lightColors(
        primary = Color(0xFF6D4C41),
        primaryVariant = Color(0xFF4E342E),
        secondary = Color(0xFF8D6E63),
        background = Color(0xFFD7CCC8),
        surface = Color(0xFFFFFFFF),
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black
    )

    AppTheme.OCEANIC -> lightColors(
        primary = Color(0xFF0277BD),
        primaryVariant = Color(0xFF01579B),
        secondary = Color(0xFF4DD0E1),
        background = Color(0xFFE0F7FA),
        surface = Color(0xFFFFFFFF),
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black
    )

    AppTheme.DESERT_SUNSET -> lightColors(
        primary = Color(0xFFFF8A65),
        primaryVariant = Color(0xFFFF7043),
        secondary = Color(0xFFFFD54F),
        background = Color(0xFFFFF3E0),
        surface = Color(0xFFFFFFFF),
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black
    )

    AppTheme.TERMINAL_GREEN -> darkColors(
        primary = Color(0xFF00FF00),
        primaryVariant = Color(0xFF00CC00),
        secondary = Color(0xFF00AA00),
        background = Color(0xFF000000),
        surface = Color(0xFF000000),
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onBackground = Color.Green,
        onSurface = Color.Green
    )

    AppTheme.SOLARIZED_LIGHT -> lightColors(
        primary = Color(0xFF268BD2),
        primaryVariant = Color(0xFF073642),
        secondary = Color(0xFF2AA198),
        background = Color(0xFFEEE8D5),
        surface = Color(0xFFFFFFFF),
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black
    )

    AppTheme.SOLARIZED_DARK -> darkColors(
        primary = Color(0xFF859900),
        primaryVariant = Color(0xFF002B36),
        secondary = Color(0xFF2AA198),
        background = Color(0xFF002B36),
        surface = Color(0xFF073642),
        onPrimary = Color.Black,
        onSecondary = Color.White,
        onBackground = Color.White,
        onSurface = Color.White
    )

    AppTheme.STARRY_NIGHT -> darkColors(
        primary = Color(0xFFBBDEFB),
        primaryVariant = Color(0xFF0D47A1),
        secondary = Color(0xFF82B1FF),
        background = Color(0xFF0B0C2A),
        surface = Color(0xFF1A237E),
        onPrimary = Color.Black,
        onSecondary = Color.White,
        onBackground = Color.White,
        onSurface = Color.White
    )

    AppTheme.SUNSET_GLOW -> lightColors(
        primary = Color(0xFFFF6E40),          // Яркий коралл
        primaryVariant = Color(0xFFDD2C00),   // Глубокий красный
        secondary = Color(0xFFFFA726),        // Оранжевый
        background = Color(0xFFFFF3E0),       // Мягкий светло-бежевый
        surface = Color(0xFFFFF3E0),
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black
    )

    AppTheme.MORNING_FRESH -> lightColors(
        primary = Color(0xFF81C784),          // Мягкий зелёный
        primaryVariant = Color(0xFF4CAF50),   // Классический зелёный
        secondary = Color(0xFFA5D6A7),        // Пастельный зелёный
        background = Color(0xFFE8F5E9),       // Светло-зелёный фон
        surface = Color(0xFFFFFFFF),
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black
    )

    AppTheme.COZY_CABIN -> darkColors(
        primary = Color(0xFF6D4C41),          // Тёмно-коричневый
        primaryVariant = Color(0xFF5D4037),
        secondary = Color(0xFFFFCC80),        // Тёплый бежевый
        background = Color(0xFF3E2723),       // Тёмно-коричневый фон
        surface = Color(0xFF4E342E),
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.White,
        onSurface = Color.White
    )

    AppTheme.LUSH_MEADOW -> lightColors(
        primary = Color(0xFF388E3C),          // Насыщенный зелёный
        primaryVariant = Color(0xFF2E7D32),
        secondary = Color(0xFFC8E6C9),        // Светло-зелёный
        background = Color(0xFFE8F5E9),       // Светлый зелёный фон
        surface = Color(0xFFFFFFFF),
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black
    )

    AppTheme.ELECTRIC_LIME -> lightColors(
        primary = Color(0xFFCCFF00),          // Яркий лайм
        primaryVariant = Color(0xFF99CC00),   // Тёмный лайм
        secondary = Color(0xFF336600),        // Тёмно-зелёный акцент
        background = Color(0xFFFFFFFF),       // Белый фон для контраста
        surface = Color(0xFFE6FF99),          // Светло-жёлто-зелёный
        onPrimary = Color.Black,
        onSecondary = Color.White,
        onBackground = Color.Black,
        onSurface = Color.Black
    )

    AppTheme.DEEP_TWILIGHT -> darkColors(
        primary = Color(0xFF4B0082),          // Индиго (тёмно-фиолетовый)
        primaryVariant = Color(0xFF2E0854),   // Очень тёмный индиго
        secondary = Color(0xFFFF6FFF),        // Яркий розово-лиловый
        background = Color(0xFF1A001A),       // Почти чёрный с фиолетовым оттенком
        surface = Color(0xFF330033),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color.White,
        onSurface = Color.White
    )

    AppTheme.CANDY_PINK -> lightColors(
        primary = Color(0xFFFF69B4),          // Яркий розовый
        primaryVariant = Color(0xFFC71585),   // Тёмный розовый
        secondary = Color(0xFFFFB6C1),        // Светло-розовый
        background = Color(0xFFFFF0F5),       // Очень светлый розовый
        surface = Color(0xFFFFE4E1),
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black
    )

    AppTheme.COOL_AQUA -> lightColors(
        primary = Color(0xFF00CED1),          // Тёмно-аквамарин
        primaryVariant = Color(0xFF008B8B),   // Тёмный аквамарин
        secondary = Color(0xFF40E0D0),        // Бирюзовый
        background = Color(0xFFE0FFFF),       // Светлый голубой
        surface = Color(0xFFB2DFDB),
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black
    )

}