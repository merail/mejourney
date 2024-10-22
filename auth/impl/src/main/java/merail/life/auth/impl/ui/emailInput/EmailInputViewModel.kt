package merail.life.auth.impl.ui.emailInput

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
import merail.life.auth.impl.ui.emailInput.state.EmailAuthState
import merail.life.auth.impl.ui.emailInput.state.EmailValidator
import merail.life.auth.impl.ui.emailInput.state.EmailValueState
import javax.inject.Inject

@HiltViewModel
class EmailInputViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: IAuthRepository,
) : ViewModel() {

    companion object {
        private const val TAG = "EmailInputViewModel"
    }

    val email: String? = savedStateHandle[EmailInputDestination.EMAIL_ARG]

    var emailAuthState = mutableStateOf<EmailAuthState>(EmailAuthState.None)
        private set

    var emailValueState by mutableStateOf(EmailValueState(email.orEmpty()))
        private set

    private val emailValidator = EmailValidator()

    fun updateEmail(
        value: String,
    ) {
        emailValueState = emailValueState.copy(
            value = value,
            isValid = true,
        )
    }

    fun validateEmail() {
        val isEmailValid = emailValidator(emailValueState.value)
        emailValueState = emailValueState.copy(
            isValid = isEmailValid,
        )
        if (isEmailValid) {
            checkIfUserExist()
        }
    }

    private fun checkIfUserExist() = viewModelScope.launch {
        emailAuthState.value = EmailAuthState.Loading
        runCatching {
            Log.d(TAG, "Проверка существования пользователя ${emailAuthState.value}. Старт")
            authRepository.isUserExist(emailValueState.value)
        }.onFailure {
            Log.w(TAG, "Проверка существования пользователя ${emailAuthState.value}. Ошибка", it)
            emailAuthState.value = EmailAuthState.Error(it.cause)
        }.onSuccess {
            Log.d(TAG, "Проверка существования пользователя ${emailAuthState.value}. Успех")
            if (it) {
                emailAuthState.value = EmailAuthState.UserExists
            } else {
                sendOtp()
            }
        }
    }

    private fun sendOtp() = viewModelScope.launch {
        runCatching {
            Log.d(TAG, "Отправка OTP. Старт")
            authRepository.sendOtp(emailValueState.value)
        }.onFailure {
            Log.w(TAG, "Отправка OTP. Ошибка", it)
            emailAuthState.value = EmailAuthState.Error(it.cause)
        }.onSuccess {
            Log.d(TAG, "Отправка OTP. Успех")
            emailAuthState.value = EmailAuthState.OtpWasSent
        }
    }
}

