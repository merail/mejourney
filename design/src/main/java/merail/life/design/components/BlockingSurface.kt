package merail.life.design.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun BlockingSurface() {
    Surface(
        color = Color.Transparent,
        content = {},
        modifier = Modifier
            .fillMaxSize(),
    )
}