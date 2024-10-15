package merail.life.auth.impl.ui.passwordCreation

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
import merail.life.auth.impl.ui.passwordCreation.state.PasswordCreationValidator
import merail.life.auth.impl.ui.passwordCreation.state.PasswordValueState
import merail.life.auth.impl.ui.passwordCreation.state.UserCreatingState
import javax.inject.Inject

@HiltViewModel
class PasswordCreationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: IAuthRepository,
) : ViewModel() {

    companion object {
        private const val TAG = "PasswordCreationViewModel"
    }

    private val email: String = checkNotNull(savedStateHandle[PasswordCreationDestination.EMAIL_ARG])

    var userCreatingState = mutableStateOf<UserCreatingState>(UserCreatingState.None)
        private set

    var passwordValueState by mutableStateOf(PasswordValueState())
        private set

    var repeatedPasswordValueState by mutableStateOf(PasswordValueState())
        private set

    private val passwordCreationValidator = PasswordCreationValidator()

    fun updatePassword(
        value: String,
    ) {
        passwordValueState = passwordValueState.copy(
            value = value,
            isValid = true,
        )
        repeatedPasswordValueState = repeatedPasswordValueState.copy(
            isValid = true,
        )
    }

    fun updateRepeatedPassword(
        value: String,
    ) {
        repeatedPasswordValueState = repeatedPasswordValueState.copy(
            value = value,
            isValid = true,
        )
    }

    fun validate() {
        val isPasswordValid = passwordCreationValidator(passwordValueState.value)
        val isRepeatedPasswordValid = passwordValueState.value == repeatedPasswordValueState.value
        passwordValueState = passwordValueState.copy(
            isValid = isPasswordValid,
        )
        repeatedPasswordValueState = repeatedPasswordValueState.copy(
            isValid = isRepeatedPasswordValid,
        )
        if (isPasswordValid && isRepeatedPasswordValid) {
            createUser()
        }
    }

    private fun createUser() {
        viewModelScope.launch {
            userCreatingState.value = UserCreatingState.Loading
            runCatching {
                Log.d(TAG, "Создание пользователя. Старт")
                authRepository.createUser(
                    email = email,
                    password = passwordValueState.value,
                )
            }.onFailure {
                Log.w(TAG, "Создание пользователя. Ошибка", it)
                userCreatingState.value = UserCreatingState.Error(it.cause)
            }.onSuccess {
                Log.d(TAG, "Создание пользователя. Успех")
                userCreatingState.value = UserCreatingState.Success
            }
        }
    }
}

