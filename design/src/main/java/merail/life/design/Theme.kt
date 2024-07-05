package merail.life.design

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

@Composable
fun MejourneyTheme(content: @Composable () -> Unit) {
    val colors = remember {
        Colors()
    }

    val typography = remember {
        Typography
    }

    CompositionLocalProvider(
        LocalMejourneyColors provides colors,
        LocalMejourneyTypography provides typography,
    ) {
        MaterialTheme(
            colorScheme = colors.materialThemeColors,
            typography = typography.materialTypography,
        ) {
            content()
        }
    }
}

object MejourneyTheme {
    val colors: Colors
        @Composable
        @ReadOnlyComposable
        get() = LocalMejourneyColors.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalMejourneyTypography.current
}
