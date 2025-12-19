package merail.life.design.extensions

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun pureStatusBarHeight(): Dp {
    val density = LocalDensity.current

    var cached by rememberSaveable { mutableStateOf(0f) }

    val current = with(density) {
        val cutoutPx = WindowInsets.displayCutout.getTop(this)
        if (cutoutPx > 0) {
            return@with cutoutPx.toDp()
        }

        val gesturesTopPx = WindowInsets.systemGestures.getTop(this)
        if (gesturesTopPx > 0) {
            return@with gesturesTopPx.toDp()
        }

        val statusBarsPx = WindowInsets.statusBars.getTop(this)
        statusBarsPx.toDp()
    }

    if (current > 0.dp && current.value != cached) {
        cached = current.value
    }

    return if (cached > 0) {
        cached.dp
    } else {
        current
    }
}