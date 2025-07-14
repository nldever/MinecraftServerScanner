package themes

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import components.Theme.minecraftTypography

@Composable
fun getThemeTypography(theme: AppTheme) = when (theme) {
    AppTheme.LIGHT -> Typography()
    AppTheme.DARK -> Typography()
    AppTheme.MINECRAFT -> minecraftTypography

    AppTheme.RETRO_8BIT -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontSize = 16.sp
        )
    )

    AppTheme.NEON_CYBERPUNK -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 22.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    )

    AppTheme.PASTEL -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontSize = 16.sp
        )
    )

    AppTheme.GRADIENT -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontSize = 16.sp
        )
    )

    AppTheme.MONOCHROME -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontSize = 16.sp
        )
    )

    AppTheme.DARK_ACCENT -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    )

    AppTheme.NATURE -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontSize = 16.sp
        )
    )

    AppTheme.RAINBOW -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.Cursive,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.Cursive,
            fontSize = 16.sp
        )
    )

    AppTheme.VINTAGE -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontSize = 16.sp
        )
    )

    // Новые темы:

    AppTheme.PIXEL_ART -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp
        )
    )

    AppTheme.FUTURISTIC_HUD -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Light,
            fontSize = 15.sp
        )
    )

    AppTheme.MIDNIGHT_BLUE -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontSize = 16.sp
        )
    )

    AppTheme.WOODLAND -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 21.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontSize = 16.sp
        )
    )

    AppTheme.OCEANIC -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontSize = 15.sp
        )
    )

    AppTheme.DESERT_SUNSET -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontSize = 16.sp
        )
    )

    AppTheme.TERMINAL_GREEN -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp
        )
    )

    AppTheme.SOLARIZED_LIGHT -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 21.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontSize = 15.sp
        )
    )

    AppTheme.SOLARIZED_DARK -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 21.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontSize = 15.sp
        )
    )

    AppTheme.STARRY_NIGHT -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontSize = 16.sp
        )
    )

    AppTheme.SUNSET_GLOW -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    )

    AppTheme.MORNING_FRESH -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontSize = 16.sp
        )
    )

    AppTheme.COZY_CABIN -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp
        )
    )

    AppTheme.LUSH_MEADOW -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.Cursive,
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.Cursive,
            fontSize = 16.sp
        )
    )

    AppTheme.ELECTRIC_LIME -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 22.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    )

    AppTheme.DEEP_TWILIGHT -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.Serif,
            fontSize = 16.sp
        )
    )

    AppTheme.CANDY_PINK -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.Cursive,
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.Cursive,
            fontSize = 16.sp
        )
    )

    AppTheme.COOL_AQUA -> Typography(
        h6 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontSize = 15.sp
        )
    )

    AppTheme.COTTON_CANDY -> Typography(
        h6 = TextStyle(fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold, fontSize = 20.sp),
        body1 = TextStyle(fontFamily = FontFamily.Cursive, fontSize = 16.sp)
    )

    AppTheme.SPACE_RETRO -> Typography(
        h6 = TextStyle(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp),
        body1 = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 16.sp)
    )

    AppTheme.BUBBLEGUM_POP -> Typography(
        h6 = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 21.sp),
        body1 = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 16.sp)
    )

    AppTheme.CHOCOLATE_LATTE -> Typography(
        h6 = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold, fontSize = 20.sp),
        body1 = TextStyle(fontFamily = FontFamily.Serif, fontSize = 16.sp)
    )

    AppTheme.SKYLINE_PURPLE -> Typography(
        h6 = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 22.sp),
        body1 = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 16.sp)
    )

    AppTheme.SOFT_MINT -> Typography(
        h6 = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium, fontSize = 20.sp),
        body1 = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 15.sp)
    )

    AppTheme.GOLDEN_HOUR -> Typography(
        h6 = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 22.sp),
        body1 = TextStyle(fontFamily = FontFamily.Serif, fontSize = 16.sp)
    )

    AppTheme.FROSTED_BLUE -> Typography(
        h6 = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium, fontSize = 20.sp),
        body1 = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 15.sp)
    )

    AppTheme.LAVENDER_FIELD -> Typography(
        h6 = TextStyle(fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold, fontSize = 20.sp),
        body1 = TextStyle(fontFamily = FontFamily.Cursive, fontSize = 16.sp)
    )

    AppTheme.TOY_BOX -> Typography(
        h6 = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = 22.sp),
        body1 = TextStyle(fontFamily = FontFamily.Default, fontSize = 16.sp)
    )
}