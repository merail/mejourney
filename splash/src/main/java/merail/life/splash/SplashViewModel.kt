package merail.life.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import merail.life.firebase.auth.IFirebaseAuthRepository
import merail.life.firebase.data.IFirebaseRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val firebaseAuthRepository: IFirebaseAuthRepository,
    private val firebaseRepository: IFirebaseRepository,
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
        firebaseAuthRepository.authAnonymously()
    }.onFailure {
        _uiState.value = SplashUiState.Error(it)
    }

    private suspend fun getItems() = runCatching {
        firebaseRepository.getHomeItems()
    }.onFailure {
        _uiState.value = SplashUiState.Error(it)
    }.onSuccess {
        _uiState.value = SplashUiState.Success
    }
}

sealed class SplashUiState {

    data object Loading: SplashUiState()

    data class Error(val exception: Throwable): SplashUiState()

    data object Success : SplashUiState()
}
