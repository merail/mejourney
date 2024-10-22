package merail.life.design.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import merail.life.core.extensions.isNavigationBarEnabled
import merail.life.design.MejourneyTheme
import merail.life.design.styles.ButtonStyle

@Composable
fun ContinueButton(
    text: String,
    needToBlockUi: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonStyle.Primary.colors(),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(
                horizontal = 12.dp,
                vertical = if (LocalContext.current.isNavigationBarEnabled) {
                    56.dp
                } else {
                    24.dp
                },
            )
            .fillMaxWidth()
            .height(64.dp),
    ) {
        if (needToBlockUi) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp),
            )
        } else {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = MejourneyTheme.typography.titleMedium,
            )
        }
    }
}