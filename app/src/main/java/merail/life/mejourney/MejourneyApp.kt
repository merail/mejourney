package merail.life.mejourney

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import merail.life.core.errors.ErrorType
import merail.life.navigation.domain.NavigationRoute
import merail.life.navigation.graph.MejourneyNavHost

@Composable
internal fun MejourneyApp(
    navController: NavHostController = rememberNavController(),
    intentRoute: MutableState<NavigationRoute?>?,
    errorType: ErrorType?,
) {
    MejourneyNavHost(
        navController = navController,
        intentRoute = intentRoute,
        errorType = errorType,
    )
}