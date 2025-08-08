package merail.life.home.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import merail.life.data.api.model.SelectorFilterType
import merail.life.navigation.domain.NavigationRoute

fun NavController.navigateToHome(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(
        route = NavigationRoute.Home,
        builder = navOptions,
    )
}

fun NavGraphBuilder.homeScreen(
    onError: (Throwable?) -> Unit,
    navigateToSelector: (SelectorFilterType) -> Unit,
    navigateToContent: (String) -> Unit,
) {
    composable<NavigationRoute.Home> {
        HomeScreen(
            onError = onError,
            navigateToSelector = navigateToSelector,
            navigateToContent = navigateToContent,
        )
    }
}