package merail.life.mejourney.navigation

import android.view.Gravity
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindowProvider
import merail.life.core.NavigationDestination
import merail.life.design.MejourneyTheme
import merail.life.design.R
import merail.life.design.cardColors

object ErrorDestination : NavigationDestination {
    override val route = "error"

    const val ERROR_MESSAGE_ARG = "errorMessage"

    val routeWithArgs = "$route/{$ERROR_MESSAGE_ARG}"
}

@Composable
fun ErrorDialog(
    onDismiss: () -> Unit,
) {
    (LocalView.current.parent as? DialogWindowProvider)?.run {
        window.setGravity(Gravity.TOP)
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
    }

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
                    text = "Ошибка",
                    style = MejourneyTheme.typography.titleMedium,
                )
                Text(
                    text = "Что-то пошло не так",
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