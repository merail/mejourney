package merail.life.mejourney.ui.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import merail.life.mejourney.navigation.NavigationDestination
import merail.life.mejourney.ui.AppViewModelProvider

object HomeDestination : NavigationDestination {
    override val route = "home"
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val text = remember {
        mutableStateOf("")
    }

    when (val uiState = viewModel.uiState.collectAsState().value) {
        is HomeUiState.Error -> text.value = uiState.exception.message.orEmpty()
        is HomeUiState.Success -> text.value = uiState.item.url
    }

    Text(
        text = text.value,
    )
}
