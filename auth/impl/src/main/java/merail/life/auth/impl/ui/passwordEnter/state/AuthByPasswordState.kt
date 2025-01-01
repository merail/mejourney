package merail.life.auth.impl.ui.passwordEnter.state

internal sealed class AuthByPasswordState {

    data object None : AuthByPasswordState()

    data object Loading: AuthByPasswordState()

    data object InvalidPassword: AuthByPasswordState()

    data class Error(val exception: Throwable?): AuthByPasswordState()

    data object Success: AuthByPasswordState()
}

internal val AuthByPasswordState.needToBlockUi
    get() = this is AuthByPasswordState.Loading || this is AuthByPasswordState.Success