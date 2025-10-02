package merail.life.mejourney

import androidx.test.ext.junit.runners.AndroidJUnit4
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
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class MainViewModelTest {

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