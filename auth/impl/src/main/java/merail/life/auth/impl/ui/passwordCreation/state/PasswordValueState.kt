package merail.life.auth.impl.ui.passwordCreation.state

data class PasswordValueState(
    val value: String = "",
    val isValid: Boolean = true,
)

class PasswordCreationValidator {

    private val passwordRegex = Regex(
        pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$",
        option = RegexOption.IGNORE_CASE,
    )

    operator fun invoke(password: String) = passwordRegex.matches(password)
}

class PasswordAuthValidator {
    operator fun invoke(value: String) = value.isNotBlank()
}