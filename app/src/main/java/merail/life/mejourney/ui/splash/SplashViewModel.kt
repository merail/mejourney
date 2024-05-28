package merail.life.mejourney.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import merail.life.firebase.auth.IFirebaseAuthRepository
import merail.life.firebase.data.IFirebaseRepository
import merail.life.firebase.data.model.HomeModel
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
        firebaseAuthRepository.auth()
    }.onFailure {
        _uiState.value = SplashUiState.Error(it)
    }

    private suspend fun getItems() = runCatching {
        firebaseRepository.getHomeItems()
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
        val items: ImmutableList<HomeModel> = persistentListOf(),
    ): SplashUiState()
}
