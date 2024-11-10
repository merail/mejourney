package merail.life.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import merail.life.core.extensions.activity

@Composable
fun SplashScreen(
    onError: (Throwable?) -> Unit,
    navigateToAuth: (Throwable?) -> Unit,
    navigateToHome: (Throwable?) -> Unit,
    viewModel: SplashViewModel = hiltViewModel<SplashViewModel>(),
) {
    LaunchedEffect(null) {
        viewModel.getUserAuthorizationState()
    }

    val uiState = viewModel.uiState.value

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
            onError(uiState.exception)
        }
        is SplashUiState.AuthSuccess -> LaunchedEffect(null) {
            navigateToHome(null)
        }
        is SplashUiState.AuthWithEmail -> LaunchedEffect(null) {
            navigateToAuth(null)
        }
    }
}