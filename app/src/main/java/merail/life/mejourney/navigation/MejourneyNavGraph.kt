package merail.life.mejourney.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import merail.life.auth.ui.AuthDestination
import merail.life.auth.ui.AuthScreen
import merail.life.data.model.SelectorFilterType
import merail.life.home.content.ContentDestination
import merail.life.home.content.ContentScreen
import merail.life.home.main.HomeDestination
import merail.life.home.main.HomeScreen
import merail.life.home.selector.SelectorDestination
import merail.life.home.selector.SelectorScreen
import merail.life.splash.SplashDestination
import merail.life.splash.SplashScreen

@Composable
internal fun MejourneyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = SplashDestination.route,
        modifier = modifier,
    ) {
        composable(
            route = SplashDestination.route,
        ) {
            SplashScreen(
                navigateToAuth = {
                    navController.navigate(HomeDestination.route)
                    it?.let {
                        navController.navigateToError(it)
                    }
                },
            )
        }
        composable(
            route = AuthDestination.route,
        ) {
            AuthScreen(
                onError = {
                    navController.navigateToError(it)
                },
                navigateToHome = {
                    navController.navigate(HomeDestination.route)
                },
            )
        }
        composable(
            route = HomeDestination.route,
        ) {
            HomeScreen(
                onError = {
                    navController.navigateToError(it)
                },
                navigateToSelector = {
                    navController.navigate("${SelectorDestination.route}/$it")
                },
                navigateToContent = {
                    navController.navigate("${ContentDestination.route}/$it")
                },
            )
        }
        composable(
            route = SelectorDestination.routeWithArgs,
            arguments = listOf(
                element = navArgument(SelectorDestination.SELECTOR_FILTER_ARG) {
                    type = NavType.EnumType(SelectorFilterType::class.java)
                },
            ),
        ) {
            SelectorScreen(
                onError = {
                    navController.popBackStack()
                    navController.navigateToError(it)
                },
                navigateToContent = {
                    navController.navigate("${ContentDestination.route}/$it")
                },
            )
        }
        composable(
            route = ContentDestination.routeWithArgs,
            arguments = listOf(
                element = navArgument(ContentDestination.CONTENT_ID_ARG) {
                    type = NavType.StringType
                },
            ),
        ) {
            ContentScreen(
                onError = {
                    navController.popBackStack()
                    navController.navigateToError(it)
                },
            )
        }
        dialog(
            route = ErrorDestination.routeWithArgs,
            dialogProperties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
            ),
            arguments = listOf(
                element = navArgument(ErrorDestination.ERROR_MESSAGE_ARG) {
                    type = NavType.StringType
                },
            ),
        ) {
            ErrorDialog(
                onDismiss = {
                    navController.popBackStack()
                }
            )
        }
    }
}

private fun NavController.navigateToError(error: Throwable?) = navigate(
    route = "${ErrorDestination.route}/${error?.message.orEmpty()}",
) {
    launchSingleTop = true
}
