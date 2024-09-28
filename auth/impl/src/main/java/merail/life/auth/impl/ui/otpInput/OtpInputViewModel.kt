package merail.life.auth.impl.ui.otpInput

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import merail.life.auth.api.IAuthRepository
import javax.inject.Inject

@HiltViewModel
class OtpInputViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
) : ViewModel() {

    var otpState by mutableStateOf(OtpState())
        private set

    fun updateOtp(
        value: String,
    ) {
        otpState = otpState.copy(
            value = value,
            isValid = true,
        )
    }

    fun verifyOtp(): Boolean {
        val isOtpValid = authRepository.getCurrentOtp() == otpState.value.toInt()
        otpState = otpState.copy(
            isValid = isOtpValid,
        )
        return isOtpValid
    }
}

