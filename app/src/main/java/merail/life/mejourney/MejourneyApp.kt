package merail.life.mejourney

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import merail.life.mejourney.navigation.MejourneyNavHost

@Composable
internal fun MejourneyApp(
    navController: NavHostController = rememberNavController(),
) {
    MejourneyNavHost(
        navController = navController,
    )
}