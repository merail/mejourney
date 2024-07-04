package merail.life.auth.api

import android.app.Activity
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import merail.life.auth.api.model.PhoneAuthCallbackType
import merail.life.core.RequestResult

interface IFirebaseAuthRepository {

    fun authAnonymously(): Flow<RequestResult<Unit>>

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
