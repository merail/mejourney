package merail.life.home.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import merail.life.core.navigation.NavigationRoute
import merail.life.data.api.model.SelectorFilterType

@Serializable
data object HomeRoute : NavigationRoute

fun NavController.navigateToHome(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(
        route = HomeRoute,
        builder = navOptions,
    )
}

fun NavGraphBuilder.homeScreen(
    onError: (Throwable?) -> Unit,
    navigateToSelector: (SelectorFilterType) -> Unit,
    navigateToContent: (String) -> Unit,
) {
    composable<HomeRoute> {
        HomeScreen(
            onError = onError,
            navigateToSelector = navigateToSelector,
            navigateToContent = navigateToContent,
        )
    }
}