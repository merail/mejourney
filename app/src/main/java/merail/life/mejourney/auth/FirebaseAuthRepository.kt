package merail.life.mejourney.auth

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth,
) : IFirebaseAuthRepository {

    companion object {
        private const val TAG = "FirebaseAuthRepository"
    }

    override suspend fun auth(): AuthResult = firebaseAuth
        .signInAnonymously()
        .addOnFailureListener {
            Log.w(TAG, "Firebase Auth. Failure", it)
        }
        .addOnSuccessListener {
            Log.d(TAG, "Firebase Auth. Success ${firebaseAuth.currentUser?.uid}")
        }
        .await()
}