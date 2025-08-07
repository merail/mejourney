package merail.life.home.content

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import merail.life.navigation.domain.NavigationRoute

fun NavController.navigateToContent(
    contentId: String,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(
        route = NavigationRoute.Content(contentId),
        builder = navOptions,
    )
}

fun NavController.navigateToContentImmediately(
    contentId: String,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    popBackStack()
    navigate(
        route = NavigationRoute.Content(contentId),
        builder = navOptions,
    )
}

fun NavGraphBuilder.contentScreen(
    navigateToError: (Throwable?) -> Unit,
) {
    composable<NavigationRoute.Content> {
        ContentScreen(
            navigateToError = navigateToError,
        )
    }
}