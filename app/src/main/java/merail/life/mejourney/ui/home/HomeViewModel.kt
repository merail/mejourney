package merail.life.mejourney.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import merail.life.mejourney.data.HomeItem
import merail.life.mejourney.data.IFirebaseRepository
import merail.life.mejourney.data.TabFilter

class HomeViewModel(
    private val firebaseStorageRepository: IFirebaseRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.Loading)

    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        getItems(TabFilter.COMMON)
    }

    fun getItems(
        filter: TabFilter,
    ) = viewModelScope.launch {
        runCatching {
            firebaseStorageRepository.getHomeItems(filter)
        }.onFailure {
            _uiState.value = HomeUiState.Error(it)
        }.onSuccess {
            _uiState.value = HomeUiState.Success(it.toImmutableList())
        }
    }
}

sealed class HomeUiState {

    data object Loading: HomeUiState()

    data class Error(val exception: Throwable): HomeUiState()

    data class Success(
        val items: ImmutableList<HomeItem> = persistentListOf(),
    ): HomeUiState()
}
