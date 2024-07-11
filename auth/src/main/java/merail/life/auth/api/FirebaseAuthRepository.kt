package merail.life.auth.api

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import merail.life.auth.api.model.PhoneAuthCallbackType
import merail.life.core.RequestResult
import merail.life.core.extensions.flowWithResult
import merail.life.core.extensions.toUnit
import merail.life.core.toRequestResult
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : IFirebaseAuthRepository {

    companion object {

        const val TAG = "FirebaseAuthRepository"

        const val TEST_PHONE_NUMBER = "+7 999 999 99 99"

        const val TEST_SMS_CODE = "111111"
    }

    private lateinit var phoneAuthCallbacksChannel: Channel<PhoneAuthCallbackType>

    private var credentials: PhoneAuthCredential? = null

    private lateinit var verificationId: String

    private val phoneAuthCallbacks = PhoneAuthCallbacks(
        onComplete = {
            runBlocking {
                phoneAuthCallbacksChannel.send(PhoneAuthCallbackType.OnComplete)
            }
            credentials = it
        },
        onFail = {
            runBlocking {
                phoneAuthCallbacksChannel.send(PhoneAuthCallbackType.OnFail(it))
            }
        },
        onCodeSent = {
            runBlocking {
                phoneAuthCallbacksChannel.send(PhoneAuthCallbackType.OnCodeSent)
            }
            verificationId = it
        },
    )

    init {
        firebaseAuth.firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(
            TEST_PHONE_NUMBER,
            TEST_SMS_CODE,
        )
    }

    override fun authAnonymously(): Flow<RequestResult<Unit>> {
        val result = flowWithResult {
            firebaseAuth.signInAnonymously().await().toUnit()
        }.flowOn(
            context = Dispatchers.IO,
        ).onEach {
            if (it.isSuccess) {
                Log.d(TAG, "Firebase anonymous auth. Success ${firebaseAuth.currentUser?.uid}")
            } else {
                Log.w(TAG, "Firebase anonymous auth. Failure", it.exceptionOrNull())
            }
        }.map(Result<Unit>::toRequestResult)
        val start = flowOf<RequestResult<Unit>>(RequestResult.InProgress())
        return merge(result, start)
    }

    override suspend fun sendCode(
        phoneAuthCallbacksChannel: Channel<PhoneAuthCallbackType>,
        activity: Activity,
        phoneNumber: String,
    ) {
        this.phoneAuthCallbacksChannel = phoneAuthCallbacksChannel
        PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(phoneAuthCallbacks)
            .build()
            .apply {
                PhoneAuthProvider.verifyPhoneNumber(this)
            }
    }

    override suspend fun authByPhone(
        activity: Activity,
        smsCode: String,
    ) {
        val credentials = credentials ?: PhoneAuthProvider.getCredential(verificationId, smsCode)
        firebaseAuth.signInWithCredential(credentials).addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "Authentication with SMS-code. Success")
            } else {
                Log.w(TAG, "Authentication with SMS-code. Failure", task.exception)
            }
        }
    }
}