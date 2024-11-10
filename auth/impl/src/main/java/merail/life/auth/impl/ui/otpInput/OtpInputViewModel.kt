package merail.life.auth.impl.ui.otpInput

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import merail.life.auth.api.IAuthRepository
import merail.life.auth.impl.ui.otpInput.state.OtpValidator
import merail.life.auth.impl.ui.otpInput.state.OtpValueState
import merail.life.navigation.domain.NavigationRoute
import javax.inject.Inject

@HiltViewModel
class OtpInputViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: IAuthRepository,
) : ViewModel() {

    val email = savedStateHandle.toRoute<NavigationRoute.OtpInput>().email

    var otpValueState by mutableStateOf(OtpValueState())
        private set

    private val otpValidator = OtpValidator()

    fun updateOtp(
        value: String,
    ) {
        if (otpValidator(value)) {
            otpValueState = otpValueState.copy(
                value = value,
                isValid = true,
            )
        }
    }

    fun verifyOtp(): Boolean {
        val isOtpVerified = authRepository.getCurrentOtp() == otpValueState.value.toInt()
        otpValueState = otpValueState.copy(
            isValid = isOtpVerified,
        )
        return isOtpVerified
    }
}

