package merail.life.auth.impl.ui.passwordEnter.state

sealed class AuthorizingState {

    data object None : AuthorizingState()

    data object Loading: AuthorizingState()

    data object InvalidPassword: AuthorizingState()

    data class Error(val exception: Throwable?): AuthorizingState()

    data object Success: AuthorizingState()
}

val AuthorizingState.needToBlockUi
    get() = this is AuthorizingState.Loading || this is AuthorizingState.Success