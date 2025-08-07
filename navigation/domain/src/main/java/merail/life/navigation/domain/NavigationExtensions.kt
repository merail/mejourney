package merail.life.navigation.domain

import android.os.Build
import androidx.compose.runtime.MutableState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import merail.life.core.errors.ErrorType

val NavBackStackEntry.errorType: ErrorType
    get() = if (destination.hasRoute(NavigationRoute.Error::class)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(NavigationRoute.Error.ERROR_TYPE_KEY, ErrorType::class.java) as ErrorType
        } else {
            @Suppress("DEPRECATION")
            arguments?.getSerializable(NavigationRoute.Error.ERROR_TYPE_KEY) as ErrorType
        }
    } else {
        throw IllegalStateException("ErrorType available only in ErrorDestination!")
    }

fun NavController.addOnPushNotificationListener(
    intentRoute: MutableState<NavigationRoute?>?,
) = addOnDestinationChangedListener { localController, _, _ ->
    val currentContentId = localController
        .currentBackStackEntry
        ?.arguments
        ?.getString(NavigationRoute.Content.CONTENT_ID_KEY).toString()
    val intentRouteValue = intentRoute?.value
    intentRouteValue?.let {
        localController.currentBackStackEntry?.destination?.run {
            when {
                hasRoute(NavigationRoute.Home::class) -> {
                    navigate(it)
                    intentRoute.value = null
                }
                hasRoute(NavigationRoute.Selector::class) -> {
                    navigate(it)
                    intentRoute.value = null
                }
                hasRoute(NavigationRoute.Content::class) -> {
                    if (currentContentId !in (it as NavigationRoute.Content).contentId.orEmpty()) {
                        navigate(it)
                    }
                    intentRoute.value = null
                }
            }
        }
    }
}