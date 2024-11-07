package merail.life.navigation.domain

import android.content.Intent

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