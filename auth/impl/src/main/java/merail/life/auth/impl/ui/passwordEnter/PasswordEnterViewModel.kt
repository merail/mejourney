package merail.life.auth.impl.ui.passwordEnter

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import merail.life.auth.api.IAuthRepository
import merail.life.auth.impl.ui.passwordCreation.state.PasswordAuthValidator
import merail.life.auth.impl.ui.passwordCreation.state.PasswordValueState
import merail.life.auth.impl.ui.passwordEnter.state.AuthByPasswordState
import merail.life.navigation.domain.NavigationRoute
import javax.inject.Inject

@HiltViewModel
class PasswordEnterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: IAuthRepository,
) : ViewModel() {

    companion object {
        private const val TAG = "PasswordEnterViewModel"
    }

    val email = savedStateHandle.toRoute<NavigationRoute.PasswordEnter>().email

    var authByPasswordState = mutableStateOf<AuthByPasswordState>(AuthByPasswordState.None)
        private set

    var passwordValueState by mutableStateOf(PasswordValueState())
        private set

    private val passwordAuthValidator = PasswordAuthValidator()

    fun updatePassword(
        value: String,
    ) {
        passwordValueState = passwordValueState.copy(
            value = value,
            isValid = true,
        )
    }

    fun validatePassword() {
        val isPasswordValid = passwordAuthValidator(passwordValueState.value)
        passwordValueState = passwordValueState.copy(
            isValid = isPasswordValid,
        )
        if (isPasswordValid) {
            authorize()
        }
    }

    private fun authorize() {
        viewModelScope.launch {
            authByPasswordState.value = AuthByPasswordState.Loading
            runCatching {
                Log.d(TAG, "Авторизация по паролю. Старт")
                authRepository.authorizeWithEmail(
                    email = email,
                    password = passwordValueState.value,
                )
            }.onFailure {
                Log.w(TAG, "Авторизация по паролю. Ошибка", it)
                if (it is FirebaseAuthInvalidCredentialsException) {
                    passwordValueState = passwordValueState.copy(
                        isValid = false,
                    )
                    authByPasswordState.value = AuthByPasswordState.InvalidPassword
                } else {
                    authByPasswordState.value = AuthByPasswordState.Error(it.cause)
                }
            }.onSuccess {
                Log.d(TAG, "Авторизация по паролю. Успех")
                authByPasswordState.value = AuthByPasswordState.Success
            }
        }
    }
}

