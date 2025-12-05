package merail.life.mejourney

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import merail.life.auth.api.IAuthRepository
import merail.life.mejourney.state.MainAuthState
import merail.life.mejourney.useCases.AuthUseCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [MainViewModel], verifying its initialization behavior
 * and state transitions related to the authentication flow.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var authRepository: IAuthRepository
    private lateinit var authUseCase: AuthUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        authRepository = mockk()
        authUseCase = AuthUseCase(
            authRepository = authRepository,
            logger = mockk(relaxed = true),
        )
    }

    @After
    fun tearsDown() {
        Dispatchers.resetMain()
    }

    /**
     * Verifies that when the [MainViewModel] is initialized and authorization succeeds,
     * it correctly updates its state to [MainAuthState.AuthSuccess].
     */
    @Test
    fun `MainViewModel init loads successfully`() = runTest {
        coEvery {
            authRepository.authorize()
        } just Runs

        val viewModel = MainViewModel(authUseCase)

        advanceUntilIdle()

        assertTrue(viewModel.state.value is MainAuthState.AuthSuccess)
    }
}