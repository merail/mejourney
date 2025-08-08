package merail.life.navigation.graph

import android.content.Intent
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import merail.life.core.extensions.Slash
import merail.life.navigation.domain.NavigationRoute

private const val CATEGORY_KEY = "category"

fun Intent.getRouteIfExists(): NavigationRoute? {
    val category = extras?.getString(CATEGORY_KEY)
    return when (category) {
        NavigationRoute.Content.ROUTE_NAME -> {
            val contentId = extras?.getString(NavigationRoute.Content.CONTENT_ID_KEY) ?: return null
            NavigationRoute.Content(contentId)
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

    val currentContentId = currentEntry.arguments?.getString(NavigationRoute.Content.CONTENT_ID_KEY).orEmpty()

    when (currentEntry.destination.routeQualifiedName) {
        NavigationRoute.Home::class.qualifiedName -> {
            navigate(intentRoute)
        }

        NavigationRoute.Selector::class.qualifiedName -> {
            navigate(intentRoute)
        }

        NavigationRoute.Content::class.qualifiedName -> {
            if (currentContentId != (intentRoute as NavigationRoute.Content).contentId) {
                navigate(intentRoute)
            }
        }
    }
}