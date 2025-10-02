package merail.life.home

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
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
import merail.life.core.constants.TestHomeElements
import merail.life.core.errors.UnauthorizedException
import merail.life.core.mappers.RequestResult
import merail.life.data.api.IDataRepository
import merail.life.data.api.model.HomeElementModel
import merail.life.home.main.HomeLoadingState
import merail.life.home.main.HomeViewModel
import merail.life.home.main.useCases.LoadHomeElementsByTabUseCase
import merail.life.home.main.useCases.LoadHomeElementsUseCase
import merail.life.home.model.TabFilter
import merail.life.home.model.toHomeItems
import merail.life.home.model.toModel
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var dataRepository: IDataRepository
    private lateinit var authRepository: IAuthRepository
    private lateinit var loadHomeElementsUseCase: LoadHomeElementsUseCase
    private lateinit var loadHomeElementsByTabUseCase: LoadHomeElementsByTabUseCase

    private val elements = listOf(
        HomeElementModel(
            id = TestHomeElements.ID_1,
            year = TestHomeElements.YEAR_23,
            country = TestHomeElements.COUNTRY_RUSSIA,
            place = TestHomeElements.PLACE_MOSCOW,
            title = TestHomeElements.TITLE_1,
            description = TestHomeElements.DESCRIPTION_1,
            url = TestHomeElements.URL_1,
        ),
        HomeElementModel(
            id = TestHomeElements.ID_2,
            year = TestHomeElements.YEAR_23,
            country = TestHomeElements.COUNTRY_RUSSIA,
            place = TestHomeElements.PLACE_KARELIA,
            title = TestHomeElements.TITLE_2,
            description = TestHomeElements.DESCRIPTION_2,
            url = TestHomeElements.URL_2,
        ),
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        dataRepository = mockk()
        authRepository = mockk()
        loadHomeElementsUseCase = LoadHomeElementsUseCase(
            dataRepository = dataRepository,
            logger = mockk(relaxed = true),
        )
        loadHomeElementsByTabUseCase = LoadHomeElementsByTabUseCase(
            dataRepository = dataRepository,
            logger = mockk(relaxed = true),
        )

        every { authRepository.isSnowfallEnabled() } returns false
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

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
            loadHomeElementsUseCase = loadHomeElementsUseCase,
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

    @Test
    fun `HomeViewModel init returns CommonError correctly`() = runTest {
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
            loadHomeElementsUseCase = loadHomeElementsUseCase,
            loadHomeElementsByTabUseCase = loadHomeElementsByTabUseCase,
        )

        advanceUntilIdle()

        val state = viewModel.state.value

        assertTrue(state is HomeLoadingState.CommonError)
        assertEquals(throwable, (state as HomeLoadingState.CommonError).exception)
    }

    @Test
    fun `HomeViewModel init returns UnauthorizedException correctly`() = runTest {
        val throwable = UnauthorizedException()
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
            loadHomeElementsUseCase = loadHomeElementsUseCase,
            loadHomeElementsByTabUseCase = loadHomeElementsByTabUseCase,
        )

        advanceUntilIdle()

        val state = viewModel.state.value

        assertTrue(state is HomeLoadingState.UnauthorizedException)
        assertEquals(throwable, (state as HomeLoadingState.UnauthorizedException).exception)
    }

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
            loadHomeElementsUseCase = loadHomeElementsUseCase,
            loadHomeElementsByTabUseCase = loadHomeElementsByTabUseCase,
        )

        viewModel.getHomeItems(tabFilter)

        advanceUntilIdle()

        val state = viewModel.state.value

        assertTrue(state is HomeLoadingState.Success)
        assertEquals(filteredData.toHomeItems(), state.items)
    }
}