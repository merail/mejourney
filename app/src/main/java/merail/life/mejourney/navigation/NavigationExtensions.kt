package merail.life.mejourney.navigation

import android.os.Build
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
    intentRouteValue: String?,
    onIntentRouteHandle: () -> Unit,
) = addOnDestinationChangedListener { localController, _, _ ->
    val currentContentId = localController
        .currentBackStackEntry
        ?.arguments
        ?.getString(ContentDestination.CONTENT_ID_ARG).toString()
    intentRouteValue?.let {
        onIntentRouteHandle()
        if (intentRouteValue.isNotEmpty()) {
            when (localController.currentBackStackEntry?.destination?.route) {
                HomeDestination.route,
                SelectorDestination.routeWithArgs,
                -> navigate(it)
                ContentDestination.routeWithArgs -> if (currentContentId !in it) {
                    navigate(it)
                }
            }
        }
    }
//    if (localController.currentBackStackEntry?.destination?.route == HomeDestination.route
//        || localController.currentBackStackEntry?.destination?.route == SelectorDestination.routeWithArgs) {
//        if (intentRoute.value.isNotEmpty()) {
//            navigate(intentRoute.value)
//            intentRoute.value = ""
//        }
//    }
}