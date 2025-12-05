package merail.life.mejourney.navigation

import android.content.Intent
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import merail.life.core.extensions.Slash
import merail.life.core.navigation.NavigationRoute
import merail.life.home.content.navigation.ContentRoute
import merail.life.home.main.navigation.HomeRoute
import merail.life.home.selector.navigation.SelectorRoute

internal const val CATEGORY_KEY = "category"

fun Intent.getRouteIfExists(): NavigationRoute? {
    val category = extras?.getString(CATEGORY_KEY)
    return when (category) {
        ContentRoute.ROUTE_NAME -> {
            val contentId = extras?.getString(ContentRoute.CONTENT_ID_KEY) ?: return null
            ContentRoute(contentId)
        }
        else -> null
    }
}

private val NavDestination.routeQualifiedName
    get() = route?.substringBefore(String.Slash)

fun NavController.navigateFromPush(
    intentRoute: NavigationRoute,
) {
    val currentEntry = currentBackStackEntry ?: return

    val currentContentId = currentEntry.arguments?.getString(ContentRoute.CONTENT_ID_KEY).orEmpty()

    when (currentEntry.destination.routeQualifiedName) {
        HomeRoute::class.qualifiedName -> {
            navigate(intentRoute)
        }

        SelectorRoute::class.qualifiedName -> {
            navigate(intentRoute)
        }

        ContentRoute::class.qualifiedName -> {
            if (currentContentId != (intentRoute as ContentRoute).contentId) {
                navigate(intentRoute)
            }
        }
    }
}