package merail.life.auth.impl.ui.emailInput.state

sealed class OtpSendingStateState {

    data object None : OtpSendingStateState()

    data object Loading: OtpSendingStateState()

    data class Error(val exception: Throwable?): OtpSendingStateState()

    data object Success: OtpSendingStateState()
}