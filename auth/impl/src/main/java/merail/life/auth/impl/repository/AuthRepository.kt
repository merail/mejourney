package merail.life.auth.impl.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import merail.life.auth.api.IAuthRepository
import merail.life.auth.api.model.UserAuthorizationState
import merail.life.auth.impl.R
import merail.life.auth.impl.mail.EmailSender
import merail.life.auth.impl.mail.PasswordAuthenticator
import merail.life.core.extensions.toUnit
import javax.inject.Inject

internal class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
    private val emailSender: EmailSender,
) : IAuthRepository {

    companion object {
        private const val EMAIL_REGISTRATION_STATE_CONFIG_KEY = "isEmailRegistrationEnabled"
        private const val HOST_EMAIL_CONFIG_KEY = "hostEmail"
        private const val HOST_PASSWORD_CONFIG_KEY = "hostPassword"
    }

    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    override suspend fun getUserAuthorizationState() = withContext(Dispatchers.IO) {
        firebaseRemoteConfig.fetchAndActivate().await().run {
            subscribeToRemoteConfigUpdates()
            val isEmailRegistrationEnabled = isEmailRegistrationEnabled()
            if (isEmailRegistrationEnabled) {
                PasswordAuthenticator.hostEmail = getHostEmail()
                PasswordAuthenticator.hostPassword = getHostPassword()
                val isUserAuthorizedByEmail = isUserAuthorizedByEmail()
                if (isUserAuthorizedByEmail) {
                    UserAuthorizationState.AUTHORIZED
                } else {
                    UserAuthorizationState.EMAIL_AUTH
                }
            } else {
                UserAuthorizationState.ANONYMOUS_AUTH
            }
        }
    }

    override suspend fun isUserExist(
        email: String,
    ) = withContext(Dispatchers.IO) {
        // TODO: Deprecated method. Think about it!
        firebaseAuth.fetchSignInMethodsForEmail(email).await().signInMethods?.isNotEmpty() ?: false
    }

    override suspend fun sendOtp(email: String) = withContext(Dispatchers.IO) {
        emailSender.sendOtp(email)
    }

    override fun getCurrentOtp() = emailSender.getCurrentOtp()

    override suspend fun createUser(
        email: String,
        password: String,
    ) = withContext(Dispatchers.IO) {
        firebaseAuth.createUserWithEmailAndPassword(
            email,
            password,
        ).await().toUnit()
    }

    override suspend fun authorizeWithEmail(
        email: String,
        password: String,
    ) = withContext(Dispatchers.IO) {
        firebaseAuth.signInWithEmailAndPassword(
            email,
            password,
        ).await().toUnit()
    }

    override suspend fun authorizeAnonymously() = withContext(Dispatchers.IO) {
        firebaseAuth.signInAnonymously().await().toUnit()
    }

    private fun subscribeToRemoteConfigUpdates() = firebaseRemoteConfig.addOnConfigUpdateListener(
        object : ConfigUpdateListener {
            override fun onUpdate(configUpdate : ConfigUpdate) {
                firebaseRemoteConfig.activate().addOnCompleteListener {
                    PasswordAuthenticator.hostEmail = getHostEmail()
                    PasswordAuthenticator.hostPassword = getHostPassword()
                }
            }

            override fun onError(error : FirebaseRemoteConfigException) {
                error.printStackTrace()
            }
        },
    )

    private fun isEmailRegistrationEnabled() = firebaseRemoteConfig.getBoolean(EMAIL_REGISTRATION_STATE_CONFIG_KEY)

    private fun getHostEmail() = firebaseRemoteConfig.getString(HOST_EMAIL_CONFIG_KEY)

    private fun getHostPassword() = firebaseRemoteConfig.getString(HOST_PASSWORD_CONFIG_KEY)

    private fun isUserAuthorizedByEmail() = firebaseAuth.currentUser?.email.isNullOrBlank().not()
}