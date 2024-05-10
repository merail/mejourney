package merail.life.mejourney.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import merail.life.mejourney.auth.IFirebaseAuthRepository
import merail.life.mejourney.data.HomeItem
import merail.life.mejourney.data.IFirebaseStorageRepository

class HomeViewModel(
    private val firebaseAuthRepository: IFirebaseAuthRepository,
    private val firebaseStorageRepository: IFirebaseStorageRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.Success())

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
        firebaseStorageRepository.getUrl("IMG_0781.HEIC")
    }.onFailure {
        _uiState.value = HomeUiState.Error(it)
    }.onSuccess {
        _uiState.value = HomeUiState.Success(it)
    }
}

sealed class HomeUiState {

    data class Error(val exception: Throwable): HomeUiState()

    data class Success(val item: HomeItem = HomeItem()): HomeUiState()
}
