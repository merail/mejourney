package merail.life.auth.api

import android.app.Activity
import kotlinx.coroutines.channels.Channel
import merail.life.auth.api.model.PhoneAuthCallbackType

interface IFirebaseAuthRepository {

    suspend fun authAnonymously()

    suspend fun sendCode(
        phoneAuthCallbacksChannel: Channel<PhoneAuthCallbackType>,
        activity: Activity,
        phoneNumber: String,
    )

    suspend fun authByPhone(
        activity: Activity,
        smsCode: String,
    )
}
