package merail.life.home
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.coEvery
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
import merail.life.home.selector.SelectorViewModel
import merail.life.home.selector.state.SelectionState
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

    private lateinit var repository: IDataRepository
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
            id = TestHomeElements.ID_4,
            year = TestHomeElements.YEAR_24,
            country = TestHomeElements.COUNTRY_RUSSIA,
            place = TestHomeElements.PLACE_MURMANSK,
            title = TestHomeElements.TITLE_4,
            description = TestHomeElements.DESCRIPTION_4,
            url = TestHomeElements.URL_4,
        ),
        HomeElementModel(
            id = TestHomeElements.ID_5,
            year = TestHomeElements.YEAR_24,
            country = TestHomeElements.COUNTRY_RUSSIA,
            place = TestHomeElements.PLACE_MOSCOW,
            title = TestHomeElements.TITLE_5,
            description = TestHomeElements.DESCRIPTION_5,
            url = TestHomeElements.URL_5,
        ),
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        repository = mockk()
        savedStateHandle = SavedStateHandle(
            initialState = mapOf(SELECTOR_FILTER_TYPE_KEY to selectorFilterType),
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `SelectorViewModel loads content successfully`() = runTest {
        coEvery {
            repository.getHomeElementsFromDatabase(
                tabFilter = null,
                selectorFilter = selectorFilterType,
            )
        } returns flowOf(
            RequestResult.InProgress(),
            RequestResult.Success(elements),
        )

        val viewModel = SelectorViewModel(
            savedStateHandle = savedStateHandle,
            dataRepository = repository,
        )

        val result = viewModel.selectionState.take(2).toList()

        val resultLoading = result[0]

        assertTrue(resultLoading is SelectionState.Loading)

        val state = viewModel.selectionState.value

        assert(state is SelectionState.Success)
        assertEquals(TestHomeElements.ID_1, state.items.first().id)
    }
}