package merail.life.design

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.valentinilk.shimmer.LocalShimmerTheme
import merail.life.core.extensions.activity

@Composable
fun MejourneyTheme(content: @Composable () -> Unit) {
    SystemBarsColor()

    val colors = remember {
        Colors()
    }

    val typography = remember {
        Typography
    }

    val currentShimmerTheme = LocalShimmerTheme.current
    val shimmerTheme = remember {
        currentShimmerTheme.copy(
            animationSpec = infiniteRepeatable(
                animation = tween(
                    800,
                    easing = LinearEasing,
                    delayMillis = 250,
                ),
                repeatMode = RepeatMode.Restart,
            ),
        )
    }

    CompositionLocalProvider(
        LocalMejourneyColors provides colors,
        LocalMejourneyTypography provides typography,
        LocalShimmerTheme provides shimmerTheme,
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

@Composable
private fun SystemBarsColor() {
    LocalView.current.run {
        SideEffect {
            context.activity?.window?.let {
                WindowCompat.getInsetsController(it, it.decorView).apply {
                    isAppearanceLightStatusBars = false
                    isAppearanceLightNavigationBars = false
                }
            }
        }
    }
}