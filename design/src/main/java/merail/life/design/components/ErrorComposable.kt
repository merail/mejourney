package merail.life.design.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import merail.life.design.MejourneyTheme

@Composable
fun ErrorMessage(
    errorMessage: String = "NullPointerException",
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Text(
            text = errorMessage,
            color = MejourneyTheme.colors.textNegative,
        )
    }
}