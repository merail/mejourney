package merail.life.auth.impl.ui.emailInput

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import merail.life.auth.api.IAuthRepository
import merail.life.auth.impl.ui.emailInput.state.EmailState
import merail.life.auth.impl.ui.emailInput.state.EmailValidator
import merail.life.auth.impl.ui.emailInput.state.OtpSendingStateState
import javax.inject.Inject

@HiltViewModel
class EmailInputViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
) : ViewModel() {

    private val _otpSendingStateState = mutableStateOf<OtpSendingStateState>(OtpSendingStateState.None)

    val emailInputUiState = _otpSendingStateState

    var emailState by mutableStateOf(EmailState())
        private set

    private val emailValidator = EmailValidator()

    fun updateEmail(
        value: String,
    ) {
        emailState = emailState.copy(
            value = value,
            isValid = true,
        )
    }

    fun validateEmail() {
        val isEmailValid = emailValidator(emailState.value)
        emailState = emailState.copy(
            isValid = isEmailValid,
        )
        if (emailValidator(emailState.value)) {
            sendOneTimePassword()
        }
    }

    private fun sendOneTimePassword() {
        viewModelScope.launch {
            emailInputUiState.value = OtpSendingStateState.Loading
            runCatching {
                authRepository.sendOneTimePassword(emailState.value)
            }.onFailure {
                emailInputUiState.value = OtpSendingStateState.Error(it.cause)
            }.onSuccess {
                emailInputUiState.value = OtpSendingStateState.Success
            }
        }
    }
}

