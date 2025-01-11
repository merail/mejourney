package merail.life.store.api

import kotlinx.coroutines.flow.Flow

interface IStoreRepository {

    suspend fun setCurrentEmail(email: String?)

    fun getCurrentEmail(): Flow<String?>

    suspend fun setOtpSendingTimestamp(timestamp: Long?)

    fun getOtpSendingTimestamp(): Flow<Long?>
}
