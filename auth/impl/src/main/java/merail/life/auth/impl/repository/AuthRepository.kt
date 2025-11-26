package merail.life.auth.impl.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import merail.life.auth.api.IAuthRepository
import merail.life.auth.impl.R
import merail.life.core.extensions.retrySuspend
import javax.inject.Inject

internal class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
) : IAuthRepository {

    companion object {
        internal const val AUTHORIZE_RETRY_ATTEMPTS_COUNT = 2

        internal const val SNOWFALL_STATE_KEY = "isSnowfallEnabled"
    }

    private val isAuthorized = MutableStateFlow(false)

    init {
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    override suspend fun authorize() = withContext(Dispatchers.IO) {
        retrySuspend(AUTHORIZE_RETRY_ATTEMPTS_COUNT) {
            firebaseAuth.signInAnonymously().await()
            isAuthorized.value = true
        }
    }

    override fun isAuthorized() = isAuthorized

    override suspend fun isSnowfallEnabled() = withContext(Dispatchers.IO) {
        firebaseRemoteConfig.fetchAndActivate().await().run {
            firebaseRemoteConfig.getBoolean(SNOWFALL_STATE_KEY)
        }
    }
}