package merail.life.auth.impl.ui.otpInput

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import merail.life.auth.api.IAuthRepository
import merail.life.auth.impl.ui.otpInput.state.OtpResendState
import merail.life.auth.impl.ui.otpInput.state.OtpValidator
import merail.life.auth.impl.ui.otpInput.state.OtpValueState
import merail.life.navigation.domain.NavigationRoute
import merail.life.store.api.IStoreRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class OtpInputViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: IAuthRepository,
    private val storeRepository: IStoreRepository,
) : ViewModel() {

    companion object {
        private const val TAG = "OtpInputViewModel"

        private const val OTP_EXPIRED_TIME = 10L

        private const val OTP_RESEND_TIME = 5L
    }

    val email = savedStateHandle.toRoute<NavigationRoute.OtpInput>().email

    var otpValueState by mutableStateOf(OtpValueState())
        private set

    var otpResendState by mutableStateOf<OtpResendState>(OtpResendState.None)
        private set

    var otpResendRemindTime by mutableLongStateOf(OTP_RESEND_TIME)
        private set

    var isCountdownTextVisible by mutableStateOf(false)
        private set

    private val otpValidator = OtpValidator()

    private var initialTimestamp = 0L

    init {
        viewModelScope.launch {
            val emailWithSentOtp = storeRepository.getCurrentEmail().first()

            storeRepository.getOtpSendingTimestamp().first().run {
                initialTimestamp = System.currentTimeMillis()
                otpResendRemindTime = when {
                    emailWithSentOtp != email -> OTP_RESEND_TIME
                    this == null -> OTP_RESEND_TIME
                    OTP_RESEND_TIME - subtractInitialInSeconds() > 0 -> OTP_RESEND_TIME - subtractInitialInSeconds()
                    else -> OTP_RESEND_TIME
                }
                isCountdownTextVisible = true
            }

            storeRepository.setCurrentEmail(email)
            storeRepository.setOtpSendingTimestamp(initialTimestamp)
            startOtpResendCountdown()
        }
    }

    fun updateOtp(
        value: String,
    ) {
        if (otpValidator(value)) {
            otpValueState = otpValueState.copy(
                value = value,
                isOtpNotExpired = true,
                isOtpVerified = true,
            )
        }
    }

    fun verifyOtp(): Boolean {
        val isOtpNotExpired = OTP_EXPIRED_TIME - System.currentTimeMillis().subtractInitialInSeconds() > 0

        val isOtpVerified = authRepository.getCurrentOtp() == otpValueState.value.toInt()
        otpValueState = otpValueState.copy(
            isOtpNotExpired = isOtpNotExpired,
            isOtpVerified = isOtpVerified,
        )

        return isOtpNotExpired && isOtpVerified
    }

    private suspend fun startOtpResendCountdown() {
        while(otpResendRemindTime > 0) {
            delay(1000)
            otpResendRemindTime--
        }
        isCountdownTextVisible = false
    }

    fun resendOtp() = viewModelScope.launch {
        runCatching {
            Log.d(TAG, "Отправка OTP. Старт")
            otpResendState = OtpResendState.Loading
            authRepository.sendOtp(email)
        }.onFailure {
            Log.w(TAG, "Отправка OTP. Ошибка", it)
            otpResendState = OtpResendState.Error(it)
        }.onSuccess {
            Log.d(TAG, "Отправка OTP. Успех")
            otpResendState = OtpResendState.OtpWasResent
            otpResendRemindTime = OTP_RESEND_TIME
            isCountdownTextVisible = true
            initialTimestamp = System.currentTimeMillis()
            storeRepository.setOtpSendingTimestamp(initialTimestamp)
            startOtpResendCountdown()
        }
    }

    private fun Long.subtractInitialInSeconds() = TimeUnit.MILLISECONDS.toSeconds(
        this - initialTimestamp,
    )
}

