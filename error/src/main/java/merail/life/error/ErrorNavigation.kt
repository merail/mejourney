package merail.life.error

import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import merail.life.core.errors.toType
import merail.life.navigation.domain.NavigationRoute
import merail.life.navigation.domain.errorType

fun NavController.navigateToError(error: Throwable?) = navigate(
    route = NavigationRoute.Error(error.toType()),
) {
    launchSingleTop = true
}

fun NavController.closeAndNavigateToError(error: Throwable?) {
    popBackStack()
    navigateToError(error)
}

fun NavGraphBuilder.errorDialog(
    onBack: () -> Unit,
) {
    dialog<NavigationRoute.Error>(
        dialogProperties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
        ),
    ) {
        ErrorDialog(
            errorType = it.errorType,
            onDismiss = onBack,
        )
    }
}