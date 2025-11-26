package merail.life.auth.impl

import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.SuccessContinuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import merail.life.auth.impl.repository.AuthRepository
import merail.life.auth.impl.repository.AuthRepository.Companion.AUTHORIZE_RETRY_ATTEMPTS_COUNT
import merail.life.auth.impl.repository.AuthRepository.Companion.SNOWFALL_STATE_KEY
import org.junit.Before
import org.junit.Test
import java.util.concurrent.Executor

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryTest {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig

    private lateinit var authRepository: AuthRepository

    @Before
    fun setup() {
        firebaseAuth = mockk()
        firebaseRemoteConfig = mockk(relaxed = true)

        every { firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults) } returns mockk()

        authRepository = AuthRepository(
            firebaseAuth = firebaseAuth,
            firebaseRemoteConfig = firebaseRemoteConfig,
        )
    }

    @Test
    fun `authorize() success`() = runTest {
        every { firebaseAuth.signInAnonymously() } returns mockAuthTaskSuccess()

        authRepository.authorize()

        assertTrue(authRepository.isAuthorized().value)
    }

    @Test
    fun `authorize() retry once then success`() = runTest {
        every { firebaseAuth.signInAnonymously() }.returnsMany(
            mockAuthTaskError(),
            mockAuthTaskSuccess(),
        )

        authRepository.authorize()

        assertTrue(authRepository.isAuthorized().value)
    }

    @Test(expected = Exception::class)
    fun `authorize() fails after retries`() = runTest {
        every { firebaseAuth.signInAnonymously() }.returnsMany(
            *Array(AUTHORIZE_RETRY_ATTEMPTS_COUNT + 1) { mockAuthTaskError() },
        )

        authRepository.authorize()
    }

    @Test
    fun `isAuthorized() returns default false`() = runTest {
        assertFalse(authRepository.isAuthorized().first())
    }

    @Test
    fun `isSnowfallEnabled returns remote config value`() = runTest {
        val mockFetchAndActivateTaskSuccess = mockFetchAndActivateTaskSuccess()

        every { firebaseRemoteConfig.fetchAndActivate() } returns mockFetchAndActivateTaskSuccess
        every { firebaseRemoteConfig.getBoolean(SNOWFALL_STATE_KEY) } returns true

        val result = authRepository.isSnowfallEnabled()

        assertTrue(result)
    }

    private fun mockAuthTaskSuccess() = mockk<Task<AuthResult>>(relaxed = true) {
        val result = mockk<AuthResult>()

        every { isComplete } returns true
        every { isCanceled } returns false
        every { isSuccessful } returns true
        every { exception } returns null
        every { this@mockk.result } returns result

        every { addOnCompleteListener(any()) } answers {
            val listener = arg<OnCompleteListener<AuthResult>>(0)
            listener.onComplete(this@mockk)
            this@mockk
        }

        every { addOnCompleteListener(any<Executor>(), any()) } answers {
            val executor = arg<Executor>(0)
            val listener = arg<OnCompleteListener<AuthResult>>(1)
            executor.execute { listener.onComplete(this@mockk) }
            this@mockk
        }

        every { addOnSuccessListener(any()) } answers {
            arg<OnSuccessListener<AuthResult>>(0).onSuccess(result)
            this@mockk
        }

        every { addOnSuccessListener(any<Executor>(), any()) } answers {
            val executor = arg<Executor>(0)
            val listener = arg<OnSuccessListener<AuthResult>>(1)
            executor.execute { listener.onSuccess(result) }
            this@mockk
        }

        every { addOnFailureListener(any()) } returns this@mockk
        every { addOnFailureListener(any<Executor>(), any()) } returns this@mockk

        every { addOnCanceledListener(any()) } returns this@mockk
        every { addOnCanceledListener(any<Executor>(), any()) } returns this@mockk

        every { continueWith<AuthResult>(any()) } answers {
            val continuation = arg<Continuation<AuthResult, *>>(0)
            continuation.then(this@mockk)
            this@mockk
        }

        every { continueWith<AuthResult>(any<Executor>(), any()) } answers {
            val executor = arg<Executor>(0)
            val continuation = arg<Continuation<AuthResult, *>>(1)
            executor.execute { continuation.then(this@mockk) }
            this@mockk
        }

        every { continueWith<AuthResult>(any()) } answers {
            val continuation = arg<Continuation<AuthResult, Task<AuthResult>>>(0)
            continuation.then(this@mockk)
        }

        every { continueWith<AuthResult>(any<Executor>(), any()) } answers {
            val executor = arg<Executor>(0)
            val continuation = arg<Continuation<AuthResult, Task<AuthResult>>>(1)
            val res = continuation.then(this@mockk)
            executor.execute { }
            res
        }

        every { onSuccessTask<Task<AuthResult>>(any()) } answers {
            val continuation = arg<SuccessContinuation<AuthResult, Task<AuthResult>>>(0)
            continuation.then(result)
        }

        every { onSuccessTask<Task<AuthResult>>(any<Executor>(), any()) } answers {
            val executor = arg<Executor>(0)
            val continuation = arg<SuccessContinuation<AuthResult, Task<AuthResult>>>(1)
            val res = continuation.then(result)
            executor.execute { }
            res
        }
    }

    private fun mockAuthTaskError() = mockk<Task<AuthResult>>(relaxed = true) {
        val exception = Exception("Fail")

        every { isComplete } returns true
        every { isCanceled } returns false
        every { isSuccessful } returns false
        every { this@mockk.exception } returns exception
        every { result } returns null

        every { addOnCompleteListener(any()) } answers {
            val listener = arg<OnCompleteListener<AuthResult>>(0)
            listener.onComplete(this@mockk)
            this@mockk
        }

        every { addOnCompleteListener(any<Executor>(), any()) } answers {
            val executor = arg<Executor>(0)
            val listener = arg<OnCompleteListener<AuthResult>>(1)
            executor.execute { listener.onComplete(this@mockk) }
            this@mockk
        }

        every { addOnSuccessListener(any()) } returns this@mockk
        every { addOnSuccessListener(any<Executor>(), any()) } returns this@mockk

        every { addOnFailureListener(any()) } answers {
            arg<OnFailureListener>(0).onFailure(exception)
            this@mockk
        }

        every { addOnFailureListener(any<Executor>(), any()) } answers {
            val executor = arg<Executor>(0)
            val listener = arg<OnFailureListener>(1)
            executor.execute { listener.onFailure(exception) }
            this@mockk
        }

        every { addOnCanceledListener(any()) } returns this@mockk
        every { addOnCanceledListener(any<Executor>(), any()) } returns this@mockk

        every { continueWith<AuthResult>(any()) } answers {
            val continuation = arg<Continuation<AuthResult, *>>(0)
            continuation.then(this@mockk)
            this@mockk
        }

        every { continueWith<AuthResult>(any<Executor>(), any()) } answers {
            val executor = arg<Executor>(0)
            val continuation = arg<Continuation<AuthResult, *>>(1)
            executor.execute { continuation.then(this@mockk) }
            this@mockk
        }

        every { continueWithTask(any<Continuation<AuthResult, Task<AuthResult>>>()) } answers {
            val continuation = arg<Continuation<AuthResult, Task<AuthResult>>>(0)
            continuation.then(this@mockk)
        }

        every { continueWithTask(any<Executor>(), any<Continuation<AuthResult, Task<AuthResult>>>()) } answers {
            val executor = arg<Executor>(0)
            val continuation = arg<Continuation<AuthResult, Task<AuthResult>>>(1)
            val res = continuation.then(this@mockk)
            executor.execute { }
            res
        }
    }

    private fun mockFetchAndActivateTaskSuccess() = mockk<Task<Boolean>>(relaxed = true) {
        every { isComplete } returns true
        every { isSuccessful } returns true
        every { exception } returns null
        every { this@mockk.result } returns true
        every { addOnCompleteListener(any<OnCompleteListener<Boolean>>()) } answers {
            arg<OnCompleteListener<Boolean>>(0).onComplete(this@mockk)
            this@mockk
        }
        every { addOnSuccessListener(any<OnSuccessListener<Boolean>>()) } answers {
            arg<OnSuccessListener<Boolean>>(0).onSuccess(result)
            this@mockk
        }
    }
}