package components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Theme {
    val minecraftColors = darkColors(
        primary = Color(0xFF55FF55),
        primaryVariant = Color(0xFF00AA00),
        secondary = Color(0xFFFFFF55),
        background = Color(0xFF1D1D1D),
        surface = Color(0xFF2B2B2B),
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onBackground = Color.White,
        onSurface = Color.White
    )

    val minecraftTypography = Typography(
        body1 = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        ),
        button = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        ),
        subtitle1 = TextStyle(
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFFFFF55)
        )
    )

    val minecraftShapes = Shapes(
        small = RoundedCornerShape(2.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

}