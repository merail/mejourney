package merail.life.auth.impl.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import merail.life.auth.api.IAuthRepository
import merail.life.core.extensions.toUnit
import javax.inject.Inject

internal class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val emailSender: EmailSender,
) : IAuthRepository {

    override fun isUserAuthorized() = firebaseAuth.currentUser != null

    override suspend fun isUserExist(
        email: String,
    ) = withContext(Dispatchers.IO) {
        // TODO: Deprecated method. Think about it!
        firebaseAuth.fetchSignInMethodsForEmail(email).await().signInMethods?.isNotEmpty() ?: false
    }

    override suspend fun sendOtp(email: String) = withContext(Dispatchers.IO) {
        emailSender.sendOtp(email)
    }

    override fun getCurrentOtp() = emailSender.getCurrentOtp()

    override suspend fun createUser(
        email: String,
        password: String,
    ) = withContext(Dispatchers.IO) {
        firebaseAuth.createUserWithEmailAndPassword(
            email,
            password,
        ).await().toUnit()
    }
}