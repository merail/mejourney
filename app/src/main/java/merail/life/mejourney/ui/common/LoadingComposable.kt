package merail.life.mejourney.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import merail.life.mejourney.ui.theme.MejourneyTheme

@Preview
@Composable
fun Loading() {
    MejourneyTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize(),
        ) {
            CircularProgressIndicator()
        }
    }
}