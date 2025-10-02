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
import merail.life.data.api.model.ContentModel
import merail.life.home.content.ContentViewModel
import merail.life.home.content.navigation.ContentRoute
import merail.life.home.content.state.ContentLoadingState
import merail.life.home.model.toContentItem
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class ContentViewModelTest {

    companion object {
        private const val CONTENT_ID = "home_cover_9"
    }

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var dataRepository: IDataRepository
    private lateinit var savedStateHandle: SavedStateHandle

    private val model = ContentModel(
        title = TestHomeElements.CONTENT_TITLE,
        text = TestHomeElements.CONTENT_TEXT,
        imagesUrls = listOf(
            TestHomeElements.URL_1,
            TestHomeElements.URL_2,
            TestHomeElements.URL_3,
        ),
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        dataRepository = mockk()
        savedStateHandle = SavedStateHandle(
            initialState = mapOf(ContentRoute.CONTENT_ID_KEY to CONTENT_ID),
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `ContentViewModel loads content successfully`() = runTest {
        every { dataRepository.getContent(CONTENT_ID) } returns flowOf(
            RequestResult.InProgress(),
            RequestResult.Success(model),
        )

        val viewModel = ContentViewModel(
            savedStateHandle = savedStateHandle,
            dataRepository = dataRepository,
        )

        val result = viewModel.contentLoadingState.take(2).toList()

        val resultLoading = result[0]

        assertTrue(resultLoading is ContentLoadingState.Loading)

        val state = viewModel.contentLoadingState.value

        assertTrue(state is ContentLoadingState.Success)
        assertEquals(model.toContentItem(), (state as ContentLoadingState.Success).item)
    }
}