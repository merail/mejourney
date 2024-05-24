package merail.life.mejourney.ui.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import merail.life.mejourney.navigation.NavigationDestination
import merail.life.mejourney.ui.AppViewModelProvider
import merail.life.mejourney.ui.common.Error
import merail.life.mejourney.ui.common.Loading

object ContentDestination : NavigationDestination {
    override val route = "content"

    const val CONTENT_ID_ARG = "contentId"

    val routeWithArgs = "$route/{$CONTENT_ID_ARG}"
}

@Composable
fun ContentScreen(
    viewModel: ContentViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is ContentUiState.Loading -> Loading()
        is ContentUiState.Error -> Error(uiState.exception.message.orEmpty())
        is ContentUiState.Success -> Unit
    }
}