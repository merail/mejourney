package merail.life.auth.api

interface IAuthRepository {
    suspend fun authorizeAnonymously()

    fun isSnowfallEnabled(): Boolean
}
