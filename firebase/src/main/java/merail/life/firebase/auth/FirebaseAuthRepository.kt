package merail.life.firebase.auth

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import merail.life.firebase.auth.model.PhoneAuthCallbackType
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

    override suspend fun auth(
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