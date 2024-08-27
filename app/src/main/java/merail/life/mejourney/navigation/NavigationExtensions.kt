package merail.life.mejourney.navigation

import android.os.Build
import androidx.compose.runtime.MutableState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import merail.life.home.content.ContentDestination
import merail.life.home.main.HomeDestination
import merail.life.home.selector.SelectorDestination
import merail.life.mejourney.error.ErrorDestination
import merail.life.mejourney.error.ErrorType
import merail.life.mejourney.error.toType

internal fun NavController.navigateToError(error: Throwable?) = navigate(
    route = "${ErrorDestination.route}/${error.toType()}",
) {
    launchSingleTop = true
}

internal val NavBackStackEntry.errorType: ErrorType
    get() = if (destination.route == ErrorDestination.routeWithArgs) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(ErrorDestination.ERROR_TYPE_ARG, ErrorType::class.java) as ErrorType
        } else {
            arguments?.getSerializable(ErrorDestination.ERROR_TYPE_ARG) as ErrorType
        }
    } else {
        throw IllegalStateException("ErrorType available only in ErrorDestination!")
    }

internal fun NavController.addOnPushNotificationListener(
    intentRoute: MutableState<String?>,
) = addOnDestinationChangedListener { localController, _, _ ->
    val currentContentId = localController
        .currentBackStackEntry
        ?.arguments
        ?.getString(ContentDestination.CONTENT_ID_ARG).toString()
    val intentRouteValue = intentRoute.value
    intentRouteValue?.let {
        if (it.isNotEmpty()) {
            when (localController.currentBackStackEntry?.destination?.route) {
                HomeDestination.route,
                SelectorDestination.routeWithArgs,
                -> {
                    navigate(it)
                    intentRoute.value = null
                }
                ContentDestination.routeWithArgs -> {
                    if (currentContentId !in it) {
                        navigate(it)
                    }
                    intentRoute.value = null
                }
            }
        }
    }
}