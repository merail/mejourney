package merail.life.mejourney.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import merail.life.mejourney.auth.IFirebaseAuthRepository
import merail.life.mejourney.data.HomeItem
import merail.life.mejourney.data.IFirebaseRepository

class HomeViewModel(
    private val firebaseAuthRepository: IFirebaseAuthRepository,
    private val firebaseStorageRepository: IFirebaseRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.Loading)

    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        viewModelScope.launch {
            val authResult = auth()
            if (authResult.isSuccess) {
                getItems()
            }
        }
    }

    private suspend fun auth() = runCatching {
        firebaseAuthRepository.auth()
    }.onFailure {
        _uiState.value = HomeUiState.Error(it)
    }

    private suspend fun getItems() = runCatching {
        firebaseStorageRepository.getHomeItems()
    }.onFailure {
        _uiState.value = HomeUiState.Error(it)
    }.onSuccess {
        _uiState.value = HomeUiState.Success(it.toImmutableList())
    }
}

sealed class HomeUiState {

    data object Loading: HomeUiState()

    data class Error(val exception: Throwable): HomeUiState()

    data class Success(val items: ImmutableList<HomeItem> = persistentListOf()): HomeUiState()
}
