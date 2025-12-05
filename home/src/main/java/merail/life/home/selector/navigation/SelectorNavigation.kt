package merail.life.home.selector.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import merail.life.core.navigation.NavigationRoute
import merail.life.data.api.model.SelectorFilterType
import merail.life.home.selector.SelectorScreen

@Serializable
data class SelectorRoute(
    val selectorFilterType: SelectorFilterType,
) : NavigationRoute

fun NavController.navigateToSelector(
    selectorFilterType: SelectorFilterType,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(
        route = SelectorRoute(selectorFilterType),
        builder = navOptions,
    )
}

fun NavGraphBuilder.selectorScreen(
    onError: (Throwable?) -> Unit,
    navigateToContent: (String) -> Unit,
    navigateToContentImmediately: (String) -> Unit,
) {
    composable<SelectorRoute> {
        SelectorScreen(
            onError = onError,
            navigateToContent = navigateToContent,
            navigateToContentImmediately = navigateToContentImmediately,
        )
    }
}