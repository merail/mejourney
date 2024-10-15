package merail.life.auth.impl.ui.passwordEnter.state

sealed class AuthByPasswordState {

    data object None : AuthByPasswordState()

    data object Loading: AuthByPasswordState()

    data object InvalidPassword: AuthByPasswordState()

    data class Error(val exception: Throwable?): AuthByPasswordState()

    data object Success: AuthByPasswordState()
}

val AuthByPasswordState.needToBlockUi
    get() = this is AuthByPasswordState.Loading || this is AuthByPasswordState.Success