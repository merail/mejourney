package merail.life.auth.impl.ui.passwordInput

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import merail.life.auth.api.IAuthRepository
import merail.life.auth.impl.ui.state.PasswordState
import merail.life.auth.impl.ui.state.PasswordValidator
import javax.inject.Inject

@HiltViewModel
class PasswordInputViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
) : ViewModel() {
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

        }
    }
}

