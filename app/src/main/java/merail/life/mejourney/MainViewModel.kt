package merail.life.mejourney

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import merail.life.auth.api.IAuthRepository
import merail.life.core.extensions.suspendableRunCatching
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _state = MutableStateFlow<MainState>(MainState.Loading)

    val state: StateFlow<MainState> = _state

    init {
        authAnonymously()
    }

    fun authAnonymously() = viewModelScope.launch {
        suspendableRunCatching {
            Log.d(TAG, "Анонимная авторизация. Старт")
            authRepository.authorizeAnonymously()
        }.onFailure { throwable ->
            Log.w(TAG, "Анонимная авторизация. Ошибка", throwable)
            _state.update {
                MainState.Error(throwable)
            }
        }.onSuccess {
            Log.d(TAG, "Анонимная авторизация. Успех")
            _state.update {
                MainState.AuthSuccess
            }
        }
    }
}
