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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
internal class OtpInputViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: IAuthRepository,
    private val storeRepository: IStoreRepository,
) : ViewModel() {

    companion object {
        private const val TAG = "OtpInputViewModel"

        private const val OTP_EXPIRED_TIME = 300L

        private const val OTP_RESEND_TIME = 60L

        private const val OTP_BLOCK_TIME = 300L

        private const val MAX_ATTEMPTS_COUNT = 5
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

    private var otpResendCountdownJob: Job? = null

    private var attemptsCount = 0

    init {
        viewModelScope.launch {
            initialTimestamp = System.currentTimeMillis()

            otpResendCountdownJob = startOtpResendCountdown()
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
                hasAvailableAttempts = true,
            )
        }
    }

    fun verifyOtp(): Boolean {
        val isOtpNotExpired = OTP_EXPIRED_TIME - differenceBetweenCurrentAndInitialTimestamps > 0

        val isOtpVerified = authRepository.getCurrentOtp() == otpValueState.value.toInt()

        if (isOtpVerified.not() && isOtpNotExpired) {
            attemptsCount++
        }
        val hasAvailableAttempts = attemptsCount < MAX_ATTEMPTS_COUNT
        if (hasAvailableAttempts.not()) {
            otpResendCountdownJob?.cancel()
            otpResendRemindTime = OTP_BLOCK_TIME
            otpResendCountdownJob = startOtpResendCountdown()
        }

        otpValueState = otpValueState.copy(
            isOtpNotExpired = isOtpNotExpired,
            isOtpVerified = isOtpVerified,
            hasAvailableAttempts = hasAvailableAttempts,
        )

        return isValid
    }

    private fun startOtpResendCountdown() = viewModelScope.launch {
        isCountdownTextVisible = true
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
            otpResendRemindTime = OTP_RESEND_TIME
            initialTimestamp = System.currentTimeMillis()
            attemptsCount = 0
            otpValueState = otpValueState.copy(
                value = "",
                isOtpNotExpired = true,
                isOtpVerified = true,
                hasAvailableAttempts = true,
            )
            otpResendCountdownJob = startOtpResendCountdown()
            otpResendState = OtpResendState.OtpWasResent
        }
    }

    private val differenceBetweenCurrentAndInitialTimestamps: Long
        get() = TimeUnit.MILLISECONDS.toSeconds(
            System.currentTimeMillis() - initialTimestamp,
            )
}

internal val OtpInputViewModel.isValid: Boolean
    get() = otpValueState.isValid

internal val OtpInputViewModel.isInputAvailable: Boolean
    get() = otpValueState.isInputAvailable

