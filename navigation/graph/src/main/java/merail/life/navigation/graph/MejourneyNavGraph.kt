package merail.life.navigation.graph

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import merail.life.core.errors.ErrorType
import merail.life.error.closeAndNavigateToError
import merail.life.error.errorDialog
import merail.life.error.navigateToError
import merail.life.home.content.contentScreen
import merail.life.home.content.navigateToContent
import merail.life.home.content.navigateToContentImmediately
import merail.life.home.main.homeScreen
import merail.life.home.selector.navigateToSelector
import merail.life.home.selector.selectorScreen
import merail.life.navigation.domain.NavigationRoute

private const val TAG = "MejourneyNavHost"

@Composable
fun MejourneyNavHost(
    navController: NavHostController,
    intentRoute: MutableState<NavigationRoute?>?,
    errorType: ErrorType?,
    modifier: Modifier = Modifier,
) {
    intentRoute?.value?.let {
        Log.d(TAG, "Route from push: $it")

        navController.navigateFromPush(it)

        intentRoute.value = null
    }

    NavHost(
        navController = navController,
        startDestination = if (errorType == null) {
            NavigationRoute.Home
        } else {
            NavigationRoute.Error(errorType)
        },
        modifier = modifier,
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