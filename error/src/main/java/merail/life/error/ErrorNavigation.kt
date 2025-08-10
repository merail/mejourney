package merail.life.error

import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import merail.life.core.errors.ErrorType
import merail.life.core.errors.toType
import merail.life.core.navigation.NavigationRoute

@Serializable
data class ErrorRoute(
    val errorType: ErrorType,
) : NavigationRoute

fun NavController.navigateToError(error: Throwable?) = navigate(
    route = ErrorRoute(error.toType()),
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
    dialog<ErrorRoute>(
        dialogProperties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
        ),
    ) {
        val args = it.toRoute<ErrorRoute>()

        ErrorDialog(
            errorType = args.errorType,
            onDismiss = onBack,
        )
    }
}