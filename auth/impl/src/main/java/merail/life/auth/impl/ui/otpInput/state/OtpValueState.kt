package merail.life.auth.impl.ui.otpInput.state

internal data class OtpValueState(
    val value: String = "",
    val isOtpNotExpired: Boolean = true,
    val isOtpVerified: Boolean = true,
    val hasAvailableAttempts: Boolean = true,
) {
    val isValid = isOtpNotExpired && isOtpVerified && hasAvailableAttempts

    val isInputAvailable = isOtpNotExpired && hasAvailableAttempts
}

internal class OtpValidator {
    operator fun invoke(value: String) = value.isEmpty() || value.last().isDigit()
}