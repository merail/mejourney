package merail.life.mejourney.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import merail.life.auth.ui.AuthDestination
import merail.life.auth.ui.AuthScreen
import merail.life.data.data.model.HomeFilterType
import merail.life.home.content.ContentDestination
import merail.life.home.content.ContentScreen
import merail.life.home.main.HomeDestination
import merail.life.home.main.HomeScreen
import merail.life.home.selector.SelectorDestination
import merail.life.home.selector.SelectorScreen
import merail.life.splash.SplashDestination
import merail.life.splash.SplashScreen

@Composable
fun MejourneyNavHost(
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
                },
            )
        }
        composable(
            route = AuthDestination.route,
        ) {
            AuthScreen(
                navigateToHome = {
                    navController.navigate(HomeDestination.route)
                },
            )
        }
        composable(
            route = HomeDestination.route,
        ) {
            HomeScreen(
                navigateToSelector = { tabFilter, selectorFilter ->
                     navController.navigate("${SelectorDestination.route}/$tabFilter")
                },
                navigateToContent = {
                    navController.navigate("${ContentDestination.route}/$it")
                },
            )
        }
        composable(
            route = SelectorDestination.routeWithArgs,
            arguments = listOf(navArgument(SelectorDestination.TAB_FILTER_ARG) {
                type = NavType.EnumType(HomeFilterType::class.java)
            }),
        ) {
            SelectorScreen(
                navigateToContent = {
                    navController.navigate("${ContentDestination.route}/$it")
                },
            )
        }
        composable(
            route = ContentDestination.routeWithArgs,
            arguments = listOf(navArgument(ContentDestination.CONTENT_ID_ARG) {
                type = NavType.StringType
            }),
        ) {
            ContentScreen()
        }
    }
}
