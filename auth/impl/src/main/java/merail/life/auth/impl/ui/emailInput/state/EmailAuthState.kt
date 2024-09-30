package merail.life.auth.impl.ui.emailInput.state

sealed class EmailAuthState {

    data object None : EmailAuthState()

    data object Loading: EmailAuthState()

    data class Error(val exception: Throwable?): EmailAuthState()

    data object UserExists: EmailAuthState()

    data object OtpWasSent: EmailAuthState()
}

val EmailAuthState.needToBlockUi
    get() = this is EmailAuthState.Loading
            || this is EmailAuthState.UserExists
            || this is EmailAuthState.OtpWasSent