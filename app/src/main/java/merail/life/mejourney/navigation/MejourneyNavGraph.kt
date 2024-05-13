package merail.life.mejourney.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import merail.life.mejourney.ui.event.EventDestination
import merail.life.mejourney.ui.event.EventScreen
import merail.life.mejourney.ui.home.HomeDestination
import merail.life.mejourney.ui.home.HomeScreen

@Composable
fun MejourneyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier,
    ) {
        composable(
            route = HomeDestination.route,
        ) {
            HomeScreen(
                navigateToEvent = {
                    navController.navigate(EventDestination.route)
                },
            )
        }
        composable(route = EventDestination.route) {
            EventScreen()
        }
    }
}
