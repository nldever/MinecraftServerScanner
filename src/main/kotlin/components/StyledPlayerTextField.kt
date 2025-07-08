package components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun StyledPlayerTextField(
    playerName: String,
    placeholder: String = "",
    onPlayerNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = playerName,
        onValueChange = onPlayerNameChange,
        label = { Text(placeholder) },
        textStyle = MaterialTheme.typography.body1.copy(color = Color.White),
        modifier = modifier,
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = MaterialTheme.colors.primary.copy(alpha = 0.6f),
            unfocusedIndicatorColor = Color.White.copy(alpha = 0.2f),
            disabledIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colors.primary,
            textColor = Color.White
        )
    )
}