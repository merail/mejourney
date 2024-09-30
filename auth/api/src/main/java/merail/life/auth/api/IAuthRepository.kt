package merail.life.auth.api

interface IAuthRepository {

    fun checkUser(): Boolean

    suspend fun sendOtp(
        email: String,
    )

    fun getCurrentOtp(): Int

    suspend fun createUser(
        email: String,
        password: String,
    )
}
