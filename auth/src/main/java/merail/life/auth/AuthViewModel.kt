package merail.life.auth

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import merail.life.firebase.auth.FirebaseAuthRepository
import merail.life.firebase.auth.IFirebaseAuthRepository
import merail.life.firebase.auth.model.PhoneAuthCallbackType
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuthRepository: IFirebaseAuthRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<AuthUiState> = MutableStateFlow(AuthUiState.PhoneEnter)

    val uiState: StateFlow<AuthUiState> = _uiState

    var phoneNumber by mutableStateOf("")
        private set

    var smsCode by mutableStateOf("")
        private set

    fun sendCode(
        activity: Activity,
    ) {
        viewModelScope.launch {
            val phoneAuthCallbacksChannel = Channel<PhoneAuthCallbackType>()
            _uiState.value = AuthUiState.Loading
            firebaseAuthRepository.sendCode(
                phoneAuthCallbacksChannel = phoneAuthCallbacksChannel,
                activity = activity,
                phoneNumber = FirebaseAuthRepository.TEST_PHONE_NUMBER,
            )
            phoneAuthCallbacksChannel.consumeEach {
                when (it) {
                    is PhoneAuthCallbackType.OnFail -> _uiState.value = AuthUiState.Error(it.error)
                    is PhoneAuthCallbackType.OnComplete,
                    is PhoneAuthCallbackType.OnCodeSent,
                    -> _uiState.value = AuthUiState.SmsEnter
                }
            }
        }
    }

    fun updatePhoneNumber(
        phoneNumber: String,
    ) {
        this.phoneNumber = phoneNumber
    }

    fun updateSmsCode(
        smsCode: String,
    ) {
        this.smsCode = smsCode
    }

    fun auth(
        activity: Activity,
    ) {
        viewModelScope.launch {
            runCatching {
                _uiState.value = AuthUiState.Loading
                firebaseAuthRepository.authByPhone(
                    activity = activity,
                    smsCode = FirebaseAuthRepository.TEST_SMS_CODE,
                )
            }.onFailure {
                _uiState.value = AuthUiState.Error(it)
            }.onSuccess {
                _uiState.value = AuthUiState.Success
            }
        }
    }
}

sealed class AuthUiState {

    data object PhoneEnter: AuthUiState()

    data object Loading: AuthUiState()

    data class Error(val exception: Throwable): AuthUiState()

    data object SmsEnter: AuthUiState()

    data object Success: AuthUiState()
}
