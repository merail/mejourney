package merail.life.mejourney.ui.selector

import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import merail.life.mejourney.data.HomeItem

class SelectorViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<SelectorUiState> = MutableStateFlow(SelectorUiState.Loading)

    val uiState: StateFlow<SelectorUiState> = _uiState
}

sealed class SelectorUiState {

    data object Loading: SelectorUiState()

    data class Error(val exception: Throwable): SelectorUiState()

    data class Success(
        val items: ImmutableList<HomeItem> = persistentListOf(),
    ): SelectorUiState()
}
