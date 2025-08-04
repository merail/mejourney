package merail.life.splash.state

internal sealed class SplashUiState {

    data object Loading: SplashUiState()

    data class Error(val exception: Throwable?): SplashUiState()

    data object AuthSuccess : SplashUiState()
}