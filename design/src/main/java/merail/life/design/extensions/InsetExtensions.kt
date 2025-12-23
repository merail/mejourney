package merail.life.design.extensions

import android.view.View
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

@Composable
fun Modifier.robustSystemBarsPadding() = padding(
    top = getInsetHeight(InsetType.TOP),
    bottom = getInsetHeight(InsetType.BOTTOM),
)

@Composable
fun robustStatusBarHeight() = getInsetHeight(InsetType.TOP)

@Composable
fun robustNavigationBarHeight() = getInsetHeight(InsetType.BOTTOM)

@Composable
private fun getInsetHeight(
    insetType: InsetType,
): Dp {
    val view = LocalView.current
    val density = LocalDensity.current

    var height by remember { mutableStateOf(0.dp) }

    DisposableEffect(view) {
        val listener = object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                height = getInsetHeight(
                    view = v,
                    density = density,
                    insetType = insetType,
                )
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        }

        view.addOnAttachStateChangeListener(listener)

        if (view.isAttachedToWindow) {
            height = getInsetHeight(
                view = view,
                density = density,
                insetType = insetType,
            )
        }

        onDispose {
            view.removeOnAttachStateChangeListener(listener)
        }
    }

    return height
}

private fun getInsetHeight(
    view: View,
    density: Density,
    insetType: InsetType,
): Dp {
    var height = 0.dp

    ViewCompat.getRootWindowInsets(view)?.let { insets ->
        val insetHeight = if (insetType == InsetType.BOTTOM) {
            insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
        } else {
            insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
        }

        if (insetHeight > 0) {
            height = with(density) { insetHeight.toDp() }
        }
    }

    return height
}

private enum class InsetType {
    BOTTOM,
    TOP,
    ;
}