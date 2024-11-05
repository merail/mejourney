package merail.life.auth.api

import merail.life.auth.api.model.UserAuthorizationState

interface IAuthRepository {

    suspend fun getUserAuthorizationState(): UserAuthorizationState

    suspend fun isUserExist(
        email: String,
    ): Boolean

    suspend fun sendOtp(
        email: String,
    )

    fun getCurrentOtp(): Int

    suspend fun createUser(
        email: String,
        password: String,
    )

    suspend fun authorizeAnonymously()

    suspend fun authorizeWithEmail(
        email: String,
        password: String,
    )
}
