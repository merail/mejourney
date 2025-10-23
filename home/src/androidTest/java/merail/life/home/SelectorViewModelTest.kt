package merail.life.home
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import merail.life.core.constants.TestHomeElements
import merail.life.core.mappers.RequestResult
import merail.life.data.api.IDataRepository
import merail.life.data.api.model.HomeElementModel
import merail.life.data.api.model.SelectorFilterType
import merail.life.home.model.toHomeItems
import merail.life.home.selector.SelectorViewModel
import merail.life.home.selector.state.SelectionLoadingState
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class SelectorViewModelTest {

    companion object {
        private const val SELECTOR_FILTER_TYPE_KEY = "selectorFilterType"
    }

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var dataRepository: IDataRepository
    private lateinit var savedStateHandle: SavedStateHandle

    private val selectorFilterType = SelectorFilterType.COUNTRY.apply {
        value = TestHomeElements.COUNTRY_RUSSIA
    }

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
            id = TestHomeElements.ID_8,
            year = TestHomeElements.YEAR_24,
            country = TestHomeElements.COUNTRY_RUSSIA,
            place = TestHomeElements.PLACE_MURMANSK,
            title = TestHomeElements.TITLE_8,
            description = TestHomeElements.DESCRIPTION_8,
            url = TestHomeElements.URL_8,
        ),
        HomeElementModel(
            id = TestHomeElements.ID_9,
            year = TestHomeElements.YEAR_24,
            country = TestHomeElements.COUNTRY_RUSSIA,
            place = TestHomeElements.PLACE_MOSCOW,
            title = TestHomeElements.TITLE_9,
            description = TestHomeElements.DESCRIPTION_9,
            url = TestHomeElements.URL_9,
        ),
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        dataRepository = mockk()
        savedStateHandle = SavedStateHandle(
            initialState = mapOf(SELECTOR_FILTER_TYPE_KEY to selectorFilterType),
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `SelectorViewModel loads selection successfully`() = runTest {
        every {
            dataRepository.getHomeElementsFromDatabase(
                tabFilter = null,
                selectorFilter = selectorFilterType,
            )
        } returns flowOf(
            RequestResult.InProgress(),
            RequestResult.Success(elements),
        )

        val viewModel = SelectorViewModel(
            savedStateHandle = savedStateHandle,
            dataRepository = dataRepository,
        )

        val result = viewModel.selectionLoadingState.take(2).toList()

        val resultLoading = result[0]

        assertTrue(resultLoading is SelectionLoadingState.Loading)

        val state = viewModel.selectionLoadingState.value

        assert(state is SelectionLoadingState.Success)
        assertEquals(elements.toHomeItems(), state.items)
    }
}