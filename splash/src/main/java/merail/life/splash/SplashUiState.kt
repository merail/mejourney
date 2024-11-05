package merail.life.splash

sealed class SplashUiState {

    data object Loading: SplashUiState()

    data class Error(val exception: Throwable?): SplashUiState()

    data object AuthSuccess : SplashUiState()

    data object AuthWithEmail : SplashUiState()
}