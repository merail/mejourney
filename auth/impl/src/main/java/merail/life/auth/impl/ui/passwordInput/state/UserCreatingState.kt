package merail.life.auth.impl.ui.passwordInput.state

sealed class UserCreatingState {

    data object None : UserCreatingState()

    data object Loading: UserCreatingState()

    data class Error(val exception: Throwable?): UserCreatingState()

    data object Success: UserCreatingState()
}