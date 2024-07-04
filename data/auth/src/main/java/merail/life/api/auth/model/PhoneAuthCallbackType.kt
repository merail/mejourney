package merail.life.api.auth.model

sealed class PhoneAuthCallbackType {

    data object OnComplete: PhoneAuthCallbackType()

    class OnFail(val error: Throwable): PhoneAuthCallbackType()

    data object OnCodeSent: PhoneAuthCallbackType()
}