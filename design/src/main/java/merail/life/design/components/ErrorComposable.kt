package merail.life.design.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import merail.life.design.MejourneyTheme

@Preview
@Composable
fun Error(
    errorMessage: String = "NullPointerException",
) {
    MejourneyTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Text(
                text = errorMessage,
                color = Color.Red,
            )
        }
    }
}