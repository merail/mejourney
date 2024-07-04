package merail.life.auth.api

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class PhoneAuthCallbacks(
    private val onComplete: (PhoneAuthCredential) -> Unit,
    private val onFail: (FirebaseException) -> Unit,
    private val onCodeSent: (String) -> Unit,
): PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        Log.d(FirebaseAuthRepository.TAG, "Verification by phone number. Success. SMS-code: ${credential.smsCode}")
        onComplete(credential)
    }

    override fun onVerificationFailed(exception: FirebaseException) {
        Log.w(FirebaseAuthRepository.TAG, "Verification by phone number. Failure", exception)
        onFail(exception)
    }

    override fun onCodeSent(
        verificationId: String,
        token: PhoneAuthProvider.ForceResendingToken,
    ) {
        Log.d(FirebaseAuthRepository.TAG, "Verification by phone number. Code was sent. ID: $verificationId")
        onCodeSent(verificationId)
    }
}