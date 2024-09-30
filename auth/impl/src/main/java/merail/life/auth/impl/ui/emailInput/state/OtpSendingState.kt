package merail.life.auth.impl.ui.emailInput.state

sealed class OtpSendingState {

    data object None : OtpSendingState()

    data object Loading: OtpSendingState()

    data class Error(val exception: Throwable?): OtpSendingState()

    data object Success: OtpSendingState()
}