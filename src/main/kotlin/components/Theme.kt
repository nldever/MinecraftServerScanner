package components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

    @Composable
    fun SelectableTextField(label: String, value: String) {
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
            Text(label, style = MaterialTheme.typography.subtitle1)
            SelectionContainer {
                Text(
                    text = value,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x22FFFFFF))
                        .padding(6.dp)
                )
            }
        }
    }

}