package merail.life.auth.impl.ui.emailInput.state

import androidx.compose.runtime.Immutable

@Immutable
data class EmailValueState(
    val value: String = "",
    val isValid: Boolean = true,
)

class EmailValidator {

    private val emailRegex = Regex(
        pattern = "^[A-Z0-9._%!+\\-А-Я$]+@[A-Z0-9.\\-А-Я]+\\.[A-ZРФ]{2,}\$",
        option = RegexOption.IGNORE_CASE,
    )

    operator fun invoke(value: String) = emailRegex.matches(value)
}