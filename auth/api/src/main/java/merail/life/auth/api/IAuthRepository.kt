package merail.life.auth.api

interface IAuthRepository {
    suspend fun authorize()

    fun isSnowfallEnabled(): Boolean
}
