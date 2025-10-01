package merail.life.mejourney.state

internal sealed class MainAuthState {

    data object Loading: MainAuthState()

    data class Error(val exception: Throwable?): MainAuthState()

    data object AuthSuccess : MainAuthState()
}