package merail.life.auth.ui.state

data class PasswordState(
    val value: String = "",
    val isValid: Boolean = true,
)

class PasswordValidator {

    private val passwordRegex = Regex(
        pattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$",
        option = RegexOption.IGNORE_CASE,
    )

    operator fun invoke(password: String) = passwordRegex.matches(password)
}