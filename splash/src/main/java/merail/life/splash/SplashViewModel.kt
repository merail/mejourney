package merail.life.splash

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import merail.life.auth.api.IAuthRepository
import merail.life.auth.api.model.UserAuthorizationState
import merail.life.splash.state.SplashUiState
import javax.inject.Inject

@HiltViewModel
internal class SplashViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
) : ViewModel() {

    companion object {
        private const val TAG = "SplashViewModel"
    }

    var uiState by mutableStateOf<SplashUiState>(SplashUiState.Loading)
        private set

    fun getUserAuthorizationState() {
        viewModelScope.launch {
            runCatching {
                Log.d(TAG, "Получение стейта авторизации. Старт")
                authRepository.getUserAuthorizationState()
            }.onFailure {
                Log.w(TAG, "Получение стейта авторизации. Ошибка", it)
                uiState = SplashUiState.Error(it.cause)
            }.onSuccess {
                Log.d(TAG, "Получение стейта авторизации. Успех")
                when (it) {
                    UserAuthorizationState.AUTHORIZED -> uiState = SplashUiState.AuthSuccess
                    UserAuthorizationState.ANONYMOUS_AUTH -> authAnonymously()
                    UserAuthorizationState.EMAIL_AUTH -> uiState = SplashUiState.AuthWithEmail
                }
            }
        }
    }

    private suspend fun authAnonymously() {
        runCatching {
            Log.d(TAG, "Анонимная авторизация. Старт")
            authRepository.authorizeAnonymously()
        }.onFailure {
            Log.w(TAG, "Анонимная авторизация. Ошибка", it)
            uiState = SplashUiState.Error(it.cause)
        }.onSuccess {
            Log.d(TAG, "Анонимная авторизация. Успех")
            uiState = SplashUiState.AuthSuccess
        }
    }
}
