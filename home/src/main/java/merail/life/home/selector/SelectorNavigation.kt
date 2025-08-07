package merail.life.home.selector

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import merail.life.data.model.SelectorFilterType
import merail.life.navigation.domain.NavigationRoute

fun NavController.navigateToSelector(
    selectorFilterType: SelectorFilterType,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(
        route = NavigationRoute.Selector(selectorFilterType),
        builder = navOptions,
    )
}

fun NavGraphBuilder.selectorScreen(
    onError: (Throwable?) -> Unit,
    navigateToContent: (String) -> Unit,
    navigateToContentImmediately: (String) -> Unit,
) {
    composable<NavigationRoute.Selector> {
        SelectorScreen(
            onError = onError,
            navigateToContent = navigateToContent,
            navigateToContentImmediately = navigateToContentImmediately,
        )
    }
}