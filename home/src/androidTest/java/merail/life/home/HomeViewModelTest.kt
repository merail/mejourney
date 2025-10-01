package merail.life.home

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import merail.life.auth.api.IAuthRepository
import merail.life.core.constants.TestHomeElements
import merail.life.core.errors.UnauthorizedException
import merail.life.home.main.HomeLoadingState
import merail.life.home.main.HomeViewModel
import merail.life.home.main.useCases.LoadHomeElementsByTabUseCase
import merail.life.home.main.useCases.LoadHomeElementsUseCase
import merail.life.home.model.HomeItem
import merail.life.home.model.TabFilter
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var authRepository: IAuthRepository
    private lateinit var loadHomeElementsUseCase: LoadHomeElementsUseCase
    private lateinit var loadHomeElementsByTabUseCase: LoadHomeElementsByTabUseCase

    private val items = listOf(
        HomeItem(
            id = TestHomeElements.ID_1,
            year = TestHomeElements.YEAR_23,
            country = TestHomeElements.COUNTRY_RUSSIA,
            place = TestHomeElements.PLACE_MOSCOW,
            title = TestHomeElements.TITLE_1,
            description = TestHomeElements.DESCRIPTION_1,
            url = TestHomeElements.URL_1,
        ),
        HomeItem(
            id = TestHomeElements.ID_2,
            year = TestHomeElements.YEAR_23,
            country = TestHomeElements.COUNTRY_RUSSIA,
            place = TestHomeElements.PLACE_KARELIA,
            title = TestHomeElements.TITLE_2,
            description = TestHomeElements.DESCRIPTION_2,
            url = TestHomeElements.URL_2,
        ),
    ).toImmutableList()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        authRepository = mockk()
        loadHomeElementsUseCase = mockk()
        loadHomeElementsByTabUseCase = mockk()

        every { authRepository.isSnowfallEnabled() } returns false
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `HomeViewModel loads content successfully`() = runTest {
        coEvery { loadHomeElementsUseCase() } returns flowOf(
            HomeLoadingState.Loading(persistentListOf()),
            HomeLoadingState.Success(items),
        )

        val viewModel = HomeViewModel(
            authRepository = authRepository,
            loadHomeElementsUseCase = loadHomeElementsUseCase,
            loadHomeElementsByTabUseCase = loadHomeElementsByTabUseCase,
        )

        val result = viewModel.state.take(2).toList()

        val resultLoading = result[0]

        assertTrue(resultLoading is HomeLoadingState.Loading)

        val state = viewModel.state.value

        assertTrue(state is HomeLoadingState.Success)
        assertEquals(items, state.items)
    }

    @Test
    fun `HomeViewModel returns CommonError correctly`() = runTest {
        val throwable = RuntimeException("fail")
        coEvery { loadHomeElementsUseCase() } returns flowOf(
            HomeLoadingState.Loading(persistentListOf()),
            HomeLoadingState.CommonError(
                exception = throwable,
                items = persistentListOf(),
            )
        )

        val viewModel = HomeViewModel(
            authRepository = authRepository,
            loadHomeElementsUseCase = loadHomeElementsUseCase,
            loadHomeElementsByTabUseCase = loadHomeElementsByTabUseCase,
        )

        val result = viewModel.state.take(2).toList()

        val resultLoading = result[0]

        assertTrue(resultLoading is HomeLoadingState.Loading)

        val state = viewModel.state.value

        assertTrue(state is HomeLoadingState.CommonError)
        assertEquals(throwable, (state as HomeLoadingState.CommonError).exception)
    }

    @Test
    fun `HomeViewModel returns UnauthorizedException correctly`() = runTest {
        val throwable = UnauthorizedException()
        coEvery { loadHomeElementsUseCase() } returns flowOf(
            HomeLoadingState.Loading(persistentListOf()),
            HomeLoadingState.UnauthorizedException(
                exception = throwable,
                items = persistentListOf(),
            )
        )

        val viewModel = HomeViewModel(
            authRepository = authRepository,
            loadHomeElementsUseCase = loadHomeElementsUseCase,
            loadHomeElementsByTabUseCase = loadHomeElementsByTabUseCase,
        )

        val result = viewModel.state.take(2).toList()

        val resultLoading = result[0]

        assertTrue(resultLoading is HomeLoadingState.Loading)

        val state = viewModel.state.value

        assertTrue(state is HomeLoadingState.UnauthorizedException)
        assertEquals(throwable, (state as HomeLoadingState.UnauthorizedException).exception)
    }

    @Test
    fun `getHomeItems returns Success correctly`() = runTest {
        val tabFilter = TabFilter.COMMON

        coEvery { loadHomeElementsUseCase() } returns emptyFlow()
        coEvery { loadHomeElementsByTabUseCase(tabFilter) } returns flowOf(
            value = HomeLoadingState.Success(items),
        )

        val viewModel = HomeViewModel(
            authRepository = authRepository,
            loadHomeElementsUseCase = loadHomeElementsUseCase,
            loadHomeElementsByTabUseCase = loadHomeElementsByTabUseCase,
        )

        viewModel.getHomeItems(tabFilter)

        advanceUntilIdle()

        val state = viewModel.state.value

        assertTrue(state is HomeLoadingState.Success)
        assertEquals(TestHomeElements.ID_1, state.items.first().id)
    }
}