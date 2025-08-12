package merail.life.design.extensions

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun pureStatusBarHeight(): Dp {
    val density = LocalDensity.current

    with(density) {
        val cutoutPx = WindowInsets.displayCutout.getTop(density)

        if (cutoutPx > 0) {
            return cutoutPx.toDp()
        }

        val gesturesTopPx = WindowInsets.systemGestures.getTop(density)

        if (gesturesTopPx > 0) {
            return gesturesTopPx.toDp()
        }

        val statusBarsPx = WindowInsets.statusBars.getTop(density)

        return statusBarsPx.toDp()
    }
}