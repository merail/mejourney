package merail.life.mejourney.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import merail.life.core.errors.ErrorType
import merail.life.core.navigation.NavigationRoute
import merail.life.error.ErrorRoute
import merail.life.error.closeAndNavigateToError
import merail.life.error.errorDialog
import merail.life.error.navigateToError
import merail.life.home.content.contentScreen
import merail.life.home.content.navigateToContent
import merail.life.home.content.navigateToContentImmediately
import merail.life.home.main.HomeRoute
import merail.life.home.main.homeScreen
import merail.life.home.selector.navigateToSelector
import merail.life.home.selector.selectorScreen

private const val TAG = "MejourneyNavHost"

@Composable
fun MejourneyNavHost(
    navController: NavHostController = rememberNavController(),
    intentRoute: MutableState<NavigationRoute?>?,
    errorType: ErrorType?,
) {
    intentRoute?.value?.let {
        Log.d(TAG, "Route from push: $it")

        navController.navigateFromPush(it)

        intentRoute.value = null
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