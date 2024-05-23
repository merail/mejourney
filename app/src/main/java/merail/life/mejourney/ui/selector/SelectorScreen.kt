package merail.life.mejourney.ui.selector

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import merail.life.mejourney.navigation.NavigationDestination
import merail.life.mejourney.ui.AppViewModelProvider
import merail.life.mejourney.ui.common.Error
import merail.life.mejourney.ui.common.Loading

object SelectorDestination : NavigationDestination {
    override val route = "selector"
}

@Composable
fun SelectorScreen(
    viewModel: SelectorViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is SelectorUiState.Loading -> Loading()
        is SelectorUiState.Error -> Error(uiState.exception.message.orEmpty())
        is SelectorUiState.Success -> Unit
    }
}