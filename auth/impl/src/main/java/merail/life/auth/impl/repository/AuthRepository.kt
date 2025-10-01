package merail.life.auth.impl.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import merail.life.auth.api.IAuthRepository
import merail.life.auth.impl.R
import javax.inject.Inject

internal class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
) : IAuthRepository {

    companion object {
        private const val SNOWFALL_STATE_KEY = "isSnowfallEnabled"
    }

    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    override suspend fun authorize() {
        withContext(Dispatchers.IO) {
            firebaseAuth.signInAnonymously().await()
        }
    }

    override fun isSnowfallEnabled() = firebaseRemoteConfig.getBoolean(SNOWFALL_STATE_KEY)
}