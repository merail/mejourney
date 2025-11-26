package merail.life.auth.api

import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    suspend fun authorize()

    fun isAuthorized(): Flow<Boolean>

    suspend fun isSnowfallEnabled(): Boolean
}
