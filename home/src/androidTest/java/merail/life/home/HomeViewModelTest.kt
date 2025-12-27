package merail.life.home

import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import merail.life.auth.api.IAuthRepository
import merail.life.core.mappers.RequestResult
import merail.life.data.api.IDataRepository
import merail.life.data.api.model.HomeElementModel
import merail.life.domain.TestHomeElements
import merail.life.home.main.HomeLoadingState
import merail.life.home.main.HomeViewModel
import merail.life.home.main.useCases.LoadHomeElementsByTabUseCase
import merail.life.home.main.useCases.LoadHomeElementsUseCase
import merail.life.home.main.useCases.LoadSnowfallStateUseCase
import merail.life.home.model.TabFilter
import merail.life.home.model.toHomeItems
import merail.life.home.model.toModel
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * `HomeViewModelTest` Unit tests for the `HomeViewModel`. These tests validate the coordination
 * between multiple Use Cases (`LoadHomeElements`, `LoadSnowfallState`, `LoadHomeElementsByTab`)
 * to ensure the UI state accurately reflects data loading progress, errors, and filtered results.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var dataRepository: IDataRepository
    private lateinit var authRepository: IAuthRepository
    private lateinit var loadHomeElementsUseCase: LoadHomeElementsUseCase
    private lateinit var loadSnowfallStateUseCase: LoadSnowfallStateUseCase
    private lateinit var loadHomeElementsByTabUseCase: LoadHomeElementsByTabUseCase

    private val elements = listOf(
        HomeElementModel(
            id = TestHomeElements.ID_1,
            year = TestHomeElements.YEAR_23,
            country = TestHomeElements.COUNTRY_RUSSIA,
            place = TestHomeElements.PLACE_MOSCOW,
            title = TestHomeElements.TITLE_1,
            description = TestHomeElements.DESCRIPTION_1,
            imageUrl = TestHomeElements.URL_1,
        ),
        HomeElementModel(
            id = TestHomeElements.ID_2,
            year = TestHomeElements.YEAR_23,
            country = TestHomeElements.COUNTRY_RUSSIA,
            place = TestHomeElements.PLACE_KARELIA,
            title = TestHomeElements.TITLE_2,
            description = TestHomeElements.DESCRIPTION_2,
            imageUrl = TestHomeElements.URL_2,
        ),
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        savedStateHandle = SavedStateHandle()

        dataRepository = mockk()
        authRepository = mockk()
        loadHomeElementsUseCase = LoadHomeElementsUseCase(
            dataRepository = dataRepository,
            logger = mockk(relaxed = true),
        )
        loadSnowfallStateUseCase = LoadSnowfallStateUseCase(
            authRepository = authRepository,
            logger = mockk(relaxed = true),
        )
        loadHomeElementsByTabUseCase = LoadHomeElementsByTabUseCase(
            dataRepository = dataRepository,
            logger = mockk(relaxed = true),
        )

        every { authRepository.isAuthorized() } returns flowOf(true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Verifies that on initialization, the ViewModel correctly emits
     * a sequence of states: starting with an empty loader, moving to a loader
     * with cached data, and finishing with a success state.
     */
    @Test
    fun `HomeViewModel init loads successfully`() = runTest {
        every {
            dataRepository.getHomeElements()
        } returns flow {
            emit(RequestResult.InProgress())
            delay(1_000)
            emit(RequestResult.InProgress(elements))
            delay(1_000)
            emit(RequestResult.Success(elements))
        }

        val viewModel = HomeViewModel(
            authRepository = authRepository,
            savedStateHandle = savedStateHandle,
            loadHomeElementsUseCase = loadHomeElementsUseCase,
            loadSnowfallStateUseCase = loadSnowfallStateUseCase,
            loadHomeElementsByTabUseCase = loadHomeElementsByTabUseCase,
        )

        val result = viewModel.state.take(3).toList()

        val resultLoading1 = result[0]

        assertTrue(resultLoading1 is HomeLoadingState.Loading)
        assertTrue(resultLoading1.items.isEmpty())

        val resultLoading2 = result[1]

        assertTrue(resultLoading2 is HomeLoadingState.Loading)
        assertEquals(elements.toHomeItems(), resultLoading2.items)

        val resultLoading3 = result[2]

        assertTrue(resultLoading3 is HomeLoadingState.Success)
        assertEquals(resultLoading3, viewModel.state.value)
        assertEquals(elements.toHomeItems(), resultLoading3.items)
    }

    /**
     * Ensures that if the data stream encounters a failure during initialization,
     * the ViewModel transitions to an `Error` state and preserves the exception
     * for the UI to handle.
     */
    @Test
    fun `HomeViewModel init returns Error correctly`() = runTest {
        val throwable = RuntimeException("fail")
        every {
            dataRepository.getHomeElements()
        } returns flow {
            emit(RequestResult.InProgress())
            delay(1_000)
            emit(RequestResult.InProgress(elements))
            delay(1_000)
            emit(RequestResult.Error(elements, throwable))
        }

        val viewModel = HomeViewModel(
            authRepository = authRepository,
            savedStateHandle = savedStateHandle,
            loadHomeElementsUseCase = loadHomeElementsUseCase,
            loadSnowfallStateUseCase = loadSnowfallStateUseCase,
            loadHomeElementsByTabUseCase = loadHomeElementsByTabUseCase,
        )

        advanceUntilIdle()

        val state = viewModel.state.value

        assertTrue(state is HomeLoadingState.Error)
        assertEquals(throwable, (state as HomeLoadingState.Error).exception)
    }

    /**
     * Verifies that if SavedStateHandle already contains a filter (e.g., after process death),
     * the ViewModel initialization triggers [loadHomeElementsByTabUseCase] instead of
     * the default [loadHomeElementsUseCase].
     */
    @Test
    fun `HomeViewModel init with saved filter loads filtered data`() = runTest {
        val savedFilter = TabFilter.COUNTRY
        val filteredData = listOf(elements[0])

        savedStateHandle[HomeViewModel.KEY_TAB_FILTER] = savedFilter

        every {
            dataRepository.getHomeElementsFromDatabase(
                tabFilter = savedFilter.toModel(),
                selectorFilter = null,
            )
        } returns flowOf(RequestResult.Success(filteredData))

        val viewModel = HomeViewModel(
            authRepository = authRepository,
            savedStateHandle = savedStateHandle,
            loadHomeElementsUseCase = loadHomeElementsUseCase,
            loadSnowfallStateUseCase = loadSnowfallStateUseCase,
            loadHomeElementsByTabUseCase = loadHomeElementsByTabUseCase,
        )

        advanceUntilIdle()

        val state = viewModel.state.value

        assertTrue(state is HomeLoadingState.Success)
        assertEquals(filteredData.toHomeItems(), state.items)

        assertEquals(savedFilter, savedStateHandle.get<TabFilter>(HomeViewModel.KEY_TAB_FILTER))

        verify(exactly = 1) {
            dataRepository.getHomeElementsFromDatabase(savedFilter.toModel(), null)
        }
        verify(exactly = 0) {
            dataRepository.getHomeElements()
        }
    }

    /**
     * Validates that the ViewModel correctly fetches and exposes
     * the "snowfall" feature flag status from the remote configuration
     * during its setup phase.
     */
    @Test
    fun `HomeViewModel init sets snowfall state correctly`() = runTest {
        coEvery {
            dataRepository.getHomeElements().collect(any())
        } returns mockk()
        coEvery {
            authRepository.isSnowfallEnabled()
        } returns true

        val viewModel = HomeViewModel(
            authRepository = authRepository,
            savedStateHandle = savedStateHandle,
            loadHomeElementsUseCase = loadHomeElementsUseCase,
            loadSnowfallStateUseCase = loadSnowfallStateUseCase,
            loadHomeElementsByTabUseCase = loadHomeElementsByTabUseCase,
        )

        advanceUntilIdle()

        val isSnowfallEnabled = viewModel.isSnowfallEnabledState.value

        assertTrue(isSnowfallEnabled)
    }

    /**
     * Confirms that when a user filters data (e.g., by selecting a specific tab),
     * the ViewModel successfully triggers the database fetch and updates the state
     * with the filtered result set.
     */
    @Test
    fun `getHomeItems loads successfully`() = runTest {
        coEvery {
            dataRepository.getHomeElements().collect(any())
        } returns mockk()
        val tabFilter = TabFilter.COUNTRY
        val filteredData = elements.groupBy {
            it.country
        }.values.map {
            it[0]
        }
        every {
            dataRepository.getHomeElementsFromDatabase(
                tabFilter = tabFilter.toModel(),
                selectorFilter = null,
            )
        } returns flowOf(
            RequestResult.InProgress(),
            RequestResult.Success(filteredData),
        )

        val viewModel = HomeViewModel(
            authRepository = authRepository,
            savedStateHandle = savedStateHandle,
            loadHomeElementsUseCase = loadHomeElementsUseCase,
            loadSnowfallStateUseCase = loadSnowfallStateUseCase,
            loadHomeElementsByTabUseCase = loadHomeElementsByTabUseCase,
        )

        viewModel.getHomeItems(tabFilter)

        advanceUntilIdle()

        val state = viewModel.state.value

        assertTrue(state is HomeLoadingState.Success)
        assertEquals(filteredData.toHomeItems(), state.items)
    }
}