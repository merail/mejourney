package merail.life.auth.impl.ui.otpInput.state

sealed class OtpResendState {

    data object None : OtpResendState()

    data object Loading: OtpResendState()

    data class Error(val exception: Throwable?): OtpResendState()

    data object OtpWasResent: OtpResendState()
}

val OtpResendState.needToBlockUi
    get() = this is OtpResendState.Loading