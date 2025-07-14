package components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import loadImageFromUrlWithCache


@Composable
fun PlayerFullBodySkinImage(
    username: String,
    size: Int = 128,
    modifier: Modifier = Modifier
) {
    val url = "https://minotar.net/body/$username/$size.png"

    val image = loadImageFromUrlWithCache(url)

    if(image != null) {
        Box(Modifier.size(40.dp)) {
            Image(
                bitmap = image,
                contentDescription = "Player Skin for $username",
                modifier = modifier,
                contentScale = ContentScale.Fit
            )
        }
    } else {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Player Icon",
            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
            modifier = Modifier.size(48.dp).padding(12.dp)
        )
    }
}