package merail.life.mejourney.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import merail.life.mejourney.auth.IFirebaseAuthRepository
import merail.life.mejourney.data.model.HomeItem
import merail.life.mejourney.data.IFirebaseRepository

class SplashViewModel(
    private val firebaseAuthRepository: IFirebaseAuthRepository,
    private val firebaseStorageRepository: IFirebaseRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<SplashUiState> = MutableStateFlow(SplashUiState.Loading)

    val uiState: StateFlow<SplashUiState> = _uiState

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
        _uiState.value = SplashUiState.Error(it)
    }

    private suspend fun getItems() = runCatching {
        firebaseStorageRepository.getHomeItems()
    }.onFailure {
        _uiState.value = SplashUiState.Error(it)
    }.onSuccess {
        _uiState.value = SplashUiState.Success(it.toImmutableList())
    }
}

sealed class SplashUiState {

    data object Loading: SplashUiState()

    data class Error(val exception: Throwable): SplashUiState()

    data class Success(
        val items: ImmutableList<HomeItem> = persistentListOf(),
    ): SplashUiState()
}
