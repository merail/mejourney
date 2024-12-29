package merail.life.store.impl.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import merail.life.core.extensions.toUnit
import merail.life.store.api.IStoreRepository
import javax.inject.Inject

private const val DATA_STORE_NAME = "DataStore"

internal val Context.dataStore by preferencesDataStore(
    name = DATA_STORE_NAME,
)

internal class StoreRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : IStoreRepository {

    companion object {
        private val CURRENT_EMAIL = stringPreferencesKey("CURRENT_EMAIL")
        private val OTP_SENDING_TIMESTAMP = longPreferencesKey("OTP_SENDING_TIMESTAMP")
    }

    override suspend fun setCurrentEmail(
        email: String?,
    ) = withContext(Dispatchers.IO) {
        dataStore.edit { settings ->
            if (email == null) {
                settings.remove(CURRENT_EMAIL)
            } else {
                settings[CURRENT_EMAIL] = email
            }
        }.toUnit()
    }

    override fun getCurrentEmail() = dataStore.data.map { preferences ->
        preferences[CURRENT_EMAIL]
    }

    override suspend fun setOtpSendingTimestamp(
        timestamp: Long?,
    ) = withContext(Dispatchers.IO) {
        dataStore.edit { settings ->
            if (timestamp == null) {
                settings.remove(OTP_SENDING_TIMESTAMP)
            } else {
                settings[OTP_SENDING_TIMESTAMP] = timestamp
            }
        }.toUnit()
    }

    override fun getOtpSendingTimestamp() = dataStore.data.map { preferences ->
        preferences[OTP_SENDING_TIMESTAMP]
    }
}