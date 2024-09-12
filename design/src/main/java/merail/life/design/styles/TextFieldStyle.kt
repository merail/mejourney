package merail.life.design.styles

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import merail.life.design.MejourneyTheme

@Immutable
sealed class TextFieldStyle {

    @Composable
    abstract fun colors(): TextFieldColors

    data object Primary : TextFieldStyle() {
        @Composable
        override fun colors(): TextFieldColors = TextFieldDefaults.colors(
            focusedTextColor = MejourneyTheme.colors.textInversePrimary,
            unfocusedTextColor = MejourneyTheme.colors.textInversePrimary,
            unfocusedContainerColor = MejourneyTheme.colors.graphicPrimary,
            focusedContainerColor = MejourneyTheme.colors.graphicPrimary,
            cursorColor = MejourneyTheme.colors.textInversePrimary,
            focusedLabelColor = MejourneyTheme.colors.textInversePrimary,
            selectionColors = TextSelectionColors(
                backgroundColor = MejourneyTheme.colors.graphicInverseSecondary,
                handleColor = MejourneyTheme.colors.textInversePrimary,
            ),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLeadingIconColor = MejourneyTheme.colors.graphicInversePrimary,
            unfocusedLeadingIconColor = MejourneyTheme.colors.graphicInversePrimary,
            unfocusedTrailingIconColor = MejourneyTheme.colors.graphicInversePrimary,
            focusedTrailingIconColor = MejourneyTheme.colors.graphicInversePrimary,
            unfocusedLabelColor = MejourneyTheme.colors.textInversePrimary,
            disabledLabelColor = MejourneyTheme.colors.textInversePrimary,
            errorContainerColor = MejourneyTheme.colors.graphicNegativeSecondary,
            errorLeadingIconColor = MejourneyTheme.colors.graphicNegativePrimary,
            errorTextColor = MejourneyTheme.colors.textNegative,
            errorIndicatorColor = Color.Transparent,
            errorCursorColor = MejourneyTheme.colors.graphicNegativePrimary,
            errorLabelColor = MejourneyTheme.colors.textNegative,
            errorTrailingIconColor = MejourneyTheme.colors.graphicNegativePrimary,
        )
    }
}
