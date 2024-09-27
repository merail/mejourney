package merail.life.auth.impl.ui.otpInput

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import merail.life.auth.api.IAuthRepository
import javax.inject.Inject

@HiltViewModel
class OtpInputViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
) : ViewModel() {

    private val _otpState = mutableStateOf("")

    val otpState = _otpState

    fun updateOtp(
        value: String,
    ) {
        _otpState.value = value
    }
}

