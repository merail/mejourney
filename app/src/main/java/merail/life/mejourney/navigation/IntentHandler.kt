package merail.life.mejourney.navigation

import android.content.Intent
import merail.life.home.content.ContentDestination

private const val CATEGORY_KEY = "category"

internal fun Intent.getRouteIfExists(): String? {
    val category = extras?.getString(CATEGORY_KEY)
    return when (category) {
        ContentDestination.route -> {
            val contentId = extras?.getString(ContentDestination.CONTENT_ID_ARG) ?: return null
            "${ContentDestination.route}/$contentId"
        }
        else -> null
    }
}