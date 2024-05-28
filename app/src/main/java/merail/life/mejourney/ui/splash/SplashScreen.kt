package merail.life.mejourney.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import merail.life.mejourney.extensions.activity
import merail.life.mejourney.navigation.NavigationDestination
import merail.life.mejourney.ui.common.Error

object SplashDestination : NavigationDestination {
    override val route = "splash"
}

@Composable
fun SplashScreen(
    navigateToHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel<SplashViewModel>(),
) {
    val uiState = viewModel.uiState.collectAsState().value

    val activity = LocalContext.current.activity
    val splashScreen = remember {
        mutableStateOf(activity?.installSplashScreen())
    }
    splashScreen.value?.setKeepOnScreenCondition {
        uiState !is SplashUiState.Success
    }

    when (uiState) {
        is SplashUiState.Loading -> Unit
        is SplashUiState.Error -> Error(uiState.exception.message.orEmpty())
        is SplashUiState.Success -> {
            navigateToHome.invoke()
        }
    }
}