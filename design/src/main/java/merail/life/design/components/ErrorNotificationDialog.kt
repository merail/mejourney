package merail.life.design.components

import android.view.Gravity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import merail.life.design.MejourneyTheme
import merail.life.design.R
import merail.life.design.cardColors

@Composable
fun ErrorNotificationDialog() {
    val shouldShowDialog = remember { mutableStateOf(true) }
    if (shouldShowDialog.value) {
        Dialog(
            onDismissRequest = {
                shouldShowDialog.value = false
            },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
            ),
        ) {
            val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
            dialogWindowProvider.window.setGravity(Gravity.TOP)
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
                                shouldShowDialog.value = false
                            },
                    )
                }
            }
        }
    }
}