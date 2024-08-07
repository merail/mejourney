package merail.life.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import merail.life.core.NavigationDestination
import merail.life.core.extensions.activity

object SplashDestination : NavigationDestination {
    override val route = "splash"
}

@Composable
fun SplashScreen(
    navigateToAuth: (Throwable?) -> Unit,
    viewModel: SplashViewModel = hiltViewModel<SplashViewModel>(),
) {
    val uiState = viewModel.uiState.collectAsState().value

    val activity = LocalContext.current.activity
    val splashScreen by remember {
        mutableStateOf(activity?.installSplashScreen())
    }
    splashScreen?.setKeepOnScreenCondition {
        uiState is SplashUiState.Loading
    }

    when (uiState) {
        is SplashUiState.Loading -> Unit
        is SplashUiState.Error -> LaunchedEffect(null) {
            navigateToAuth(uiState.exception)
        }
        is SplashUiState.Success -> LaunchedEffect(null) {
            navigateToAuth(null)
        }
    }
}