package merail.life.mejourney.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import merail.life.mejourney.extensions.activity
import merail.life.mejourney.navigation.NavigationDestination
import merail.life.mejourney.ui.AppViewModelProvider

object SplashDestination : NavigationDestination {
    override val route = "splash"
}

@Composable
fun SplashScreen(
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = viewModel(factory = AppViewModelProvider.Factory),
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

@Composable
private fun Error(
    errorMessage: String,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Text(
            text = errorMessage,
            color = Color.Red,
        )
    }
}