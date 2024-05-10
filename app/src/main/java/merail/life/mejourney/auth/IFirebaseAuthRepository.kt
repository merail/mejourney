package merail.life.mejourney.auth

import com.google.firebase.auth.AuthResult

interface IFirebaseAuthRepository {
    suspend fun auth(): AuthResult
}
