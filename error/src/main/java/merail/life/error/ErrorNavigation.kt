package merail.life.error

import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import merail.life.core.errors.toType
import merail.life.navigation.domain.NavigationRoute

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
        val args = it.toRoute<NavigationRoute.Error>()

        ErrorDialog(
            errorType = args.errorType,
            onDismiss = onBack,
        )
    }
}