package merail.life.firebase.auth

import com.google.firebase.auth.AuthResult

interface IFirebaseAuthRepository {
    suspend fun auth(): AuthResult
}
