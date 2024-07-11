package merail.life.mejourney.navigation

import android.os.Build
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import merail.life.mejourney.error.ErrorDestination
import merail.life.mejourney.error.ErrorType
import merail.life.mejourney.error.toType

internal fun NavController.navigateToError(error: Throwable?) = navigate(
    route = "${ErrorDestination.route}/${error.toType()}",
) {
    launchSingleTop = true
}

internal val NavBackStackEntry.errorType: ErrorType
    get() = if (destination.route == ErrorDestination.route) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(ErrorDestination.ERROR_TYPE_ARG, ErrorType::class.java) as ErrorType
        } else {
            arguments?.getSerializable(ErrorDestination.ERROR_TYPE_ARG) as ErrorType
        }
    } else {
        throw IllegalStateException("ErrorType available only in ErrorDestination!")
    }