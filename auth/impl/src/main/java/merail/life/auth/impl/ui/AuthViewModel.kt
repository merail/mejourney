package merail.life.auth.impl.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import merail.life.auth.api.IAuthRepository
import merail.life.auth.impl.ui.state.EmailState
import merail.life.auth.impl.ui.state.EmailValidator
import merail.life.auth.impl.ui.state.PasswordState
import merail.life.auth.impl.ui.state.PasswordValidator
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
) : ViewModel() {

    var emailState by mutableStateOf(EmailState())
        private set

    var passwordState by mutableStateOf(PasswordState())
        private set

    var repeatedPasswordState by mutableStateOf(PasswordState())
        private set

    private val emailValidator = EmailValidator()

    private val passwordValidator = PasswordValidator()

    private val emailSender = EmailSender()

    fun updateEmail(
        value: String,
    ) {
        emailState = emailState.copy(
            value = value,
            isValid = true,
        )
    }

    fun updatePassword(
        value: String,
    ) {
        passwordState = passwordState.copy(
            value = value,
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
        val isEmailValid = emailValidator(emailState.value)
        val isPasswordValid = passwordValidator(passwordState.value)
        val isRepeatedPasswordValid = passwordState.value == repeatedPasswordState.value
        emailState = emailState.copy(
            isValid = isEmailValid,
        )
        passwordState = passwordState.copy(
            isValid = isPasswordValid,
        )
        repeatedPasswordState = repeatedPasswordState.copy(
            isValid = isRepeatedPasswordValid,
        )
        if (isEmailValid && isPasswordValid && isRepeatedPasswordValid) {
            createUser()
        }
    }

    private fun createUser() {
        viewModelScope.launch {
            emailSender.sendEmail(emailState.value)
//            authRepository.createUser(
//                email = emailState.value,
//                password = passwordState.value,
//            ).collect {
//
//            }
        }
    }
}

