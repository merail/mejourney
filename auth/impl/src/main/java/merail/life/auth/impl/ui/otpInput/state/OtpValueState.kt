package merail.life.auth.impl.ui.otpInput.state

data class OtpValueState(
    val value: String = "",
    val isValid: Boolean = true,
)

class OtpValidator {
    operator fun invoke(value: String) = value.isEmpty() || value.last().isDigit()
}