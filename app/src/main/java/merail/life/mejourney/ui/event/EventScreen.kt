package merail.life.mejourney.ui.event

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import merail.life.mejourney.navigation.NavigationDestination
import merail.life.mejourney.ui.AppViewModelProvider
import merail.life.mejourney.ui.home.common.Error
import merail.life.mejourney.ui.home.common.Loading

object EventDestination : NavigationDestination {
    override val route = "event"
}

@Composable
fun EventScreen(
    modifier: Modifier = Modifier,
    viewModel: EventViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is EventUiState.Loading -> Loading()
        is EventUiState.Error -> Error(uiState.exception.message.orEmpty())
        is EventUiState.Success -> Unit
    }
}