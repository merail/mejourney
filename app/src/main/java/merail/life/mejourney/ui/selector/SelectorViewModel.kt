package merail.life.mejourney.ui.selector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import merail.life.firebase.data.IFirebaseRepository
import merail.life.firebase.data.model.HomeModel
import merail.life.firebase.data.model.HomeFilterType
import javax.inject.Inject

@HiltViewModel
class SelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val firebaseRepository: IFirebaseRepository,
) : ViewModel() {

    private val tabFilter: HomeFilterType = checkNotNull(savedStateHandle[SelectorDestination.TAB_FILTER_ARG])

    private val _uiState: MutableStateFlow<SelectorUiState> = MutableStateFlow(SelectorUiState.Loading)

    val uiState: StateFlow<SelectorUiState> = _uiState

    init {
        getItems(tabFilter)
    }

    private fun getItems(
        filter: HomeFilterType,
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
        val items: ImmutableList<HomeModel> = persistentListOf(),
    ): SelectorUiState()
}
