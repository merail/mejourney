package merail.life.error

import android.view.Gravity
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindowProvider
import merail.life.core.errors.ErrorType
import merail.life.design.MejourneyTheme
import merail.life.design.cardColors

@Composable
fun ErrorDialog(
    errorType: ErrorType,
    onDismiss: () -> Unit,
) {
    (LocalView.current.parent as? DialogWindowProvider)?.run {
        window.setGravity(Gravity.TOP)
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
    }

    val state = rememberSwipeToDismissBoxState()

    when (state.targetValue) {
        SwipeToDismissBoxValue.StartToEnd,
        SwipeToDismissBoxValue.EndToStart,
        -> onDismiss()
        SwipeToDismissBoxValue.Settled,
        -> Unit
    }

    SwipeToDismissBox(
        state = state,
        backgroundContent = {},
    ) {
        Card(
            colors = MejourneyTheme.colors.cardColors.copy(
                containerColor = MejourneyTheme.colors.elementNegative,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                ),
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp),
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                ) {
                    Text(
                        text = stringResource(R.string.error_title),
                        style = MejourneyTheme.typography.titleMedium,
                    )
                    Text(
                        text = stringResource(errorType.message),
                        style = MejourneyTheme.typography.bodyMedium,
                    )
                }

                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_cross),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Top)
                        .clickable {
                            onDismiss()
                        },
                )
            }
        }
    }
}

private val ErrorType.message: Int
    get() = when (this) {
        ErrorType.INTERNET_CONNECTION -> R.string.error_internet_connection_subtitle
        ErrorType.OTHER -> R.string.error_common_subtitle
    }