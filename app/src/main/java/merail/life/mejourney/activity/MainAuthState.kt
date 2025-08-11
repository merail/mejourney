package merail.life.mejourney.activity

internal sealed class MainAuthState {

    data object Loading: MainAuthState()

    data class Error(val exception: Throwable?): MainAuthState()

    data object AuthSuccess : MainAuthState()
}