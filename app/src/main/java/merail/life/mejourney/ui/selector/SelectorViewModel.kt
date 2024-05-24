package merail.life.mejourney.ui.selector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import merail.life.mejourney.data.model.HomeItem
import merail.life.mejourney.data.IFirebaseRepository
import merail.life.mejourney.data.model.TabFilter

class SelectorViewModel(
    savedStateHandle: SavedStateHandle,
    private val firebaseRepository: IFirebaseRepository,
) : ViewModel() {

    private val tabFilter: TabFilter = checkNotNull(savedStateHandle[SelectorDestination.TAB_FILTER_ARG])

    private val _uiState: MutableStateFlow<SelectorUiState> = MutableStateFlow(SelectorUiState.Loading)

    val uiState: StateFlow<SelectorUiState> = _uiState

    init {
        getItems(tabFilter)
    }

    private fun getItems(
        filter: TabFilter,
    ) = viewModelScope.launch {
        runCatching {
            firebaseRepository.getHomeItems(filter)
        }.onFailure {
            _uiState.value = SelectorUiState.Error(it)
        }.onSuccess {
            _uiState.value = SelectorUiState.Success(it.toImmutableList())
        }
    }
}

sealed class SelectorUiState {

    data object Loading: SelectorUiState()

    data class Error(val exception: Throwable): SelectorUiState()

    data class Success(
        val items: ImmutableList<HomeItem> = persistentListOf(),
    ): SelectorUiState()
}
