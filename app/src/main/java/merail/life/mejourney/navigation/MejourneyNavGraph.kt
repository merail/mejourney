package merail.life.mejourney.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import merail.life.firebase.data.model.HomeFilterType
import merail.life.mejourney.ui.content.ContentDestination
import merail.life.mejourney.ui.content.ContentScreen
import merail.life.mejourney.ui.home.HomeDestination
import merail.life.mejourney.ui.home.HomeScreen
import merail.life.mejourney.ui.selector.SelectorDestination
import merail.life.mejourney.ui.selector.SelectorScreen
import merail.life.mejourney.ui.splash.SplashDestination
import merail.life.mejourney.ui.splash.SplashScreen

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
                navigateToHome = {
                    navController.navigate(HomeDestination.route)
                },
            )
        }
        composable(
            route = HomeDestination.route,
        ) {
            HomeScreen(
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
