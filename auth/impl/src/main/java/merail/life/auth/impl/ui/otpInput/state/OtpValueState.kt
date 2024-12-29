package merail.life.auth.impl.ui.otpInput.state

data class OtpValueState(
    val value: String = "",
    val isOtpNotExpired: Boolean = true,
    val isOtpVerified: Boolean = true,
) {
    val isValid = isOtpNotExpired && isOtpVerified
}

class OtpValidator {
    operator fun invoke(value: String) = value.isEmpty() || value.last().isDigit()
}