package merail.life.auth.api

import kotlinx.coroutines.flow.Flow
import merail.life.core.RequestResult

interface IAuthRepository {

    fun checkUser(): Flow<RequestResult<Boolean>>

    fun createUser(
        email: String,
        password: String,
    ): Flow<RequestResult<Unit>>
}
