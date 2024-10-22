package merail.life.splash

import merail.life.core.RequestResult

sealed class SplashUiState {

    data object Loading: SplashUiState()

    data class Error(val exception: Throwable?): SplashUiState()

    data class Success(
        val isRegistered: Boolean,
    ) : SplashUiState()
}

internal fun RequestResult<Boolean>.toState() = when (this) {
    is RequestResult.InProgress -> SplashUiState.Loading
    is RequestResult.Error -> SplashUiState.Error(
        exception = error,
    )
    is RequestResult.Success -> SplashUiState.Success(
        isRegistered = data,
    )
}