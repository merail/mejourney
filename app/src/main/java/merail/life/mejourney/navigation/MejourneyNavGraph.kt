package merail.life.mejourney.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import merail.life.core.errors.ErrorType
import merail.life.core.log.IMejourneyLogger
import merail.life.core.navigation.NavigationRoute
import merail.life.error.ErrorRoute
import merail.life.error.closeAndNavigateToError
import merail.life.error.errorDialog
import merail.life.error.navigateToError
import merail.life.home.content.navigation.contentScreen
import merail.life.home.content.navigation.navigateToContent
import merail.life.home.content.navigation.navigateToContentImmediately
import merail.life.home.main.navigation.HomeRoute
import merail.life.home.main.navigation.homeScreen
import merail.life.home.selector.navigation.navigateToSelector
import merail.life.home.selector.navigation.selectorScreen

private const val TAG = "MejourneyNavHost"

@Composable
fun MejourneyNavHost(
    navController: NavHostController = rememberNavController(),
    logger: IMejourneyLogger,
    intentRoute: MutableState<NavigationRoute?>?,
    errorType: ErrorType?,
) {
    LaunchedEffect(intentRoute?.value, navController.currentBackStackEntry) {
        val route = intentRoute?.value ?: return@LaunchedEffect
        if (navController.currentBackStackEntry != null) {
            logger.d(TAG, "Route from push: $route")

            navController.navigateFromPush(route)

            intentRoute.value = null
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (errorType == null) {
            HomeRoute
        } else {
            ErrorRoute(errorType)
        },
    ) {
        homeScreen(
            onError = navController::navigateToError,
            navigateToSelector = navController::navigateToSelector,
            navigateToContent = navController::navigateToContent,
        )

        selectorScreen(
            onError = navController::closeAndNavigateToError,
            navigateToContent = navController::navigateToContent,
            navigateToContentImmediately = navController::navigateToContentImmediately,
        )

        contentScreen(
            navigateToError = navController::closeAndNavigateToError,
        )

        errorDialog(
            onBack = navController::popBackStack,
        )
    }
}