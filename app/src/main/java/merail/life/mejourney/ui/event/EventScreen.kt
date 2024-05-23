package merail.life.mejourney.ui.event

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import merail.life.mejourney.navigation.NavigationDestination
import merail.life.mejourney.ui.AppViewModelProvider
import merail.life.mejourney.ui.common.Error
import merail.life.mejourney.ui.common.Loading

object EventDestination : NavigationDestination {
    override val route = "event"
}

@Composable
fun EventScreen(
    viewModel: EventViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is EventUiState.Loading -> Loading()
        is EventUiState.Error -> Error(uiState.exception.message.orEmpty())
        is EventUiState.Success -> Unit
    }
}