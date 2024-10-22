package merail.life.splash

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import merail.life.core.NavigationDestination

object SplashDestination : NavigationDestination {
    override val route = "splash"
}

@Composable
fun SplashScreen(
    navigateToAuth: (Throwable?) -> Unit,
    navigateToHome: (Throwable?) -> Unit,
    viewModel: SplashViewModel = hiltViewModel<SplashViewModel>(),
) {
    if (viewModel.isRegistered) {
        navigateToHome(null)
    } else {
        navigateToAuth(null)
    }
}