package merail.life.auth.impl.ui.passwordInput

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import merail.life.auth.api.IAuthRepository
import merail.life.auth.impl.ui.passwordInput.state.PasswordState
import merail.life.auth.impl.ui.passwordInput.state.PasswordValidator
import merail.life.auth.impl.ui.passwordInput.state.UserCreatingState
import javax.inject.Inject

@HiltViewModel
class PasswordInputViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: IAuthRepository,
) : ViewModel() {

    companion object {
        private const val TAG = "PasswordInputViewModel"
    }

    private val _userCreatingState = mutableStateOf<UserCreatingState>(UserCreatingState.None)

    val userCreatingState = _userCreatingState

    private val email: String = checkNotNull(savedStateHandle[PasswordInputDestination.EMAIL_ARG])

    var passwordState by mutableStateOf(PasswordState())
        private set

    var repeatedPasswordState by mutableStateOf(PasswordState())
        private set

    private val passwordValidator = PasswordValidator()

    fun updatePassword(
        value: String,
    ) {
        passwordState = passwordState.copy(
            value = value,
            isValid = true,
        )
        repeatedPasswordState = repeatedPasswordState.copy(
            isValid = true,
        )
    }

    fun updateRepeatedPassword(
        value: String,
    ) {
        repeatedPasswordState = repeatedPasswordState.copy(
            value = value,
            isValid = true,
        )
    }

    fun validate() {
        val isPasswordValid = passwordValidator(passwordState.value)
        val isRepeatedPasswordValid = passwordState.value == repeatedPasswordState.value
        passwordState = passwordState.copy(
            isValid = isPasswordValid,
        )
        repeatedPasswordState = repeatedPasswordState.copy(
            isValid = isRepeatedPasswordValid,
        )
        if (isPasswordValid && isRepeatedPasswordValid) {
            createUser()
        }
    }

    private fun createUser() {
        viewModelScope.launch {
            Log.d(TAG, "Создание пользователя. Старт")
            _userCreatingState.value = UserCreatingState.Loading
            runCatching {
                authRepository.createUser(
                    email = email,
                    password = passwordState.value,
                )
            }.onFailure {
                Log.w(TAG, "Создание пользователя. Ошибка", it)
                _userCreatingState.value = UserCreatingState.Error(it.cause)
            }.onSuccess {
                Log.d(TAG, "Создание пользователя. Успех")
                _userCreatingState.value = UserCreatingState.Success
            }
        }
    }
}

