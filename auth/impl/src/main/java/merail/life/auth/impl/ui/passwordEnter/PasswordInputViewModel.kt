package merail.life.auth.impl.ui.passwordEnter

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import merail.life.auth.api.IAuthRepository
import merail.life.auth.impl.ui.passwordEnter.state.AuthorizingState
import merail.life.auth.impl.ui.passwordEnter.state.PasswordState
import javax.inject.Inject

@HiltViewModel
class PasswordEnterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: IAuthRepository,
) : ViewModel() {

    companion object {
        private const val TAG = "PasswordEnterViewModel"
    }

    private val _authorizingState = mutableStateOf<AuthorizingState>(AuthorizingState.None)

    val authorizingState = _authorizingState

    private val email: String = checkNotNull(savedStateHandle[PasswordEnterDestination.EMAIL_ARG])

    var passwordState by mutableStateOf(PasswordState())
        private set

    fun updatePassword(
        value: String,
    ) {
        passwordState = passwordState.copy(
            value = value,
            isValid = true,
        )
    }

    fun authorize() {
        viewModelScope.launch {
            Log.d(TAG, "Авторизация. Старт")
            _authorizingState.value = AuthorizingState.Loading
            runCatching {
                authRepository.authorize(
                    email = email,
                    password = passwordState.value,
                )
            }.onFailure {
                Log.w(TAG, "Авторизация. Ошибка", it)
                if (it is FirebaseAuthInvalidCredentialsException) {
                    passwordState = passwordState.copy(
                        isValid = false,
                    )
                    _authorizingState.value = AuthorizingState.InvalidPassword
                } else {
                    _authorizingState.value = AuthorizingState.Error(it.cause)
                }
            }.onSuccess {
                Log.d(TAG, "Авторизация. Успех")
                _authorizingState.value = AuthorizingState.Success
            }
        }
    }
}

