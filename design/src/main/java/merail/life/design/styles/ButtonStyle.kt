package merail.life.design.styles

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import merail.life.design.ColorConstants
import merail.life.design.MejourneyTheme

@Immutable
sealed class ButtonStyle {

    @Composable
    abstract fun colors(): ButtonColors

    data object Primary : ButtonStyle() {
        @Composable
        override fun colors() = ButtonDefaults.buttonColors(
            containerColor = ColorConstants.thunder,
            contentColor = MejourneyTheme.colors.textPrimary,
            disabledContainerColor = ColorConstants.thunder,
            disabledContentColor = MejourneyTheme.colors.textPrimary,
        )
    }
}
