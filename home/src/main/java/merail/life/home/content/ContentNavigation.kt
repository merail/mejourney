package merail.life.home.content

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import merail.life.core.navigation.NavigationRoute

@Serializable
data class ContentRoute(
    val contentId: String,
) : NavigationRoute {
    companion object {
        const val ROUTE_NAME = "content"
        const val CONTENT_ID_KEY = "contentId"
    }
}

fun NavController.navigateToContent(
    contentId: String,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(
        route = ContentRoute(contentId),
        builder = navOptions,
    )
}

fun NavController.navigateToContentImmediately(
    contentId: String,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    popBackStack()
    navigate(
        route = ContentRoute(contentId),
        builder = navOptions,
    )
}

fun NavGraphBuilder.contentScreen(
    navigateToError: (Throwable?) -> Unit,
) {
    composable<ContentRoute> {
        ContentScreen(
            navigateToError = navigateToError,
        )
    }
}