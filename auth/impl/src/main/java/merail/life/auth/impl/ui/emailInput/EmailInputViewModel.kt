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
import merail.life.auth.impl.ui.emailInput.state.OtpSendingState
import javax.inject.Inject

@HiltViewModel
class EmailInputViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
) : ViewModel() {

    private val _otpSendingState = mutableStateOf<OtpSendingState>(OtpSendingState.None)

    val otpSendingState = _otpSendingState

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
        if (isEmailValid) {
            sendOtp()
        }
    }

    private fun sendOtp() {
        viewModelScope.launch {
            _otpSendingState.value = OtpSendingState.Loading
            runCatching {
                authRepository.sendOtp(emailState.value)
            }.onFailure {
                _otpSendingState.value = OtpSendingState.Error(it.cause)
            }.onSuccess {
                _otpSendingState.value = OtpSendingState.Success
            }
        }
    }
}

