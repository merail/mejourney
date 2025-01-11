package merail.life.auth.impl.ui.passwordCreation.state

internal sealed class UserCreatingState {

    data object None : UserCreatingState()

    data object Loading: UserCreatingState()

    data class Error(val exception: Throwable?): UserCreatingState()

    data object Success: UserCreatingState()
}

internal val UserCreatingState.needToBlockUi
    get() = this is UserCreatingState.Loading || this is UserCreatingState.Success