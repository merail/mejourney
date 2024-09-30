package merail.life.auth.impl.ui.emailInput

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import merail.life.auth.api.IAuthRepository
import merail.life.auth.impl.ui.emailInput.state.EmailAuthState
import merail.life.auth.impl.ui.emailInput.state.EmailState
import merail.life.auth.impl.ui.emailInput.state.EmailValidator
import javax.inject.Inject

@HiltViewModel
class EmailInputViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
) : ViewModel() {

    private val _emailAuthState = mutableStateOf<EmailAuthState>(EmailAuthState.None)

    val emailAuthState = _emailAuthState

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
            checkIfUserExist()
        }
    }

    private fun checkIfUserExist() = viewModelScope.launch {
        _emailAuthState.value = EmailAuthState.Loading
        runCatching {
            authRepository.isUserExist(emailState.value)
        }.onFailure {
            _emailAuthState.value = EmailAuthState.Error(it.cause)
        }.onSuccess {
            if (it) {
                _emailAuthState.value = EmailAuthState.UserExists
            } else {
                sendOtp()
            }
        }
    }

    private fun sendOtp() = viewModelScope.launch {
        runCatching {
            authRepository.sendOtp(emailState.value)
        }.onFailure {
            _emailAuthState.value = EmailAuthState.Error(it.cause)
        }.onSuccess {
            _emailAuthState.value = EmailAuthState.OtpWasSent
        }
    }
}

