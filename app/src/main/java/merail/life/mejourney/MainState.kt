package merail.life.mejourney

internal sealed class MainState {

    data object Loading: MainState()

    data class Error(val exception: Throwable?): MainState()

    data object AuthSuccess : MainState()
}