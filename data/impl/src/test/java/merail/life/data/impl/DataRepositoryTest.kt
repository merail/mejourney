package merail.life.data.impl

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import merail.life.core.constants.TestHomeElements
import merail.life.core.mappers.RequestResult
import merail.life.data.api.IDataRepository
import merail.life.data.api.model.HomeFilterType
import merail.life.data.api.model.SelectorFilterType
import merail.life.data.impl.database.HomeDatabase
import merail.life.data.impl.database.HomeElementDao
import merail.life.data.impl.database.dto.HomeElementEntity
import merail.life.data.impl.server.ServerApi
import merail.life.data.impl.server.ServerRepository
import merail.life.data.impl.server.dto.ContentDto
import merail.life.data.impl.server.dto.CoverDto
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DataRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var homeDatabase: HomeDatabase
    private lateinit var homeElementDao: HomeElementDao
    private lateinit var serverApi: ServerApi
    private lateinit var serverRepository: ServerRepository
    private lateinit var dataRepository: IDataRepository

    private val coverEntities = listOf(
        HomeElementEntity(
            id = TestHomeElements.ID_1,
            year = TestHomeElements.YEAR_23,
            country = TestHomeElements.COUNTRY_RUSSIA,
            place = TestHomeElements.PLACE_MOSCOW,
            title = TestHomeElements.TITLE_1,
            description = TestHomeElements.DESCRIPTION_1,
            url = TestHomeElements.URL_1,
        ),
        HomeElementEntity(
            id = TestHomeElements.ID_7,
            year = TestHomeElements.YEAR_23,
            country = TestHomeElements.COUNTRY_TURKEY,
            place = TestHomeElements.PLACE_CAPPADOCIA,
            title = TestHomeElements.TITLE_7,
            description = TestHomeElements.DESCRIPTION_7,
            url = TestHomeElements.URL_7,
        ),
        HomeElementEntity(
            id = TestHomeElements.ID_9,
            year = TestHomeElements.YEAR_24,
            country = TestHomeElements.COUNTRY_RUSSIA,
            place = TestHomeElements.PLACE_MOSCOW,
            title = TestHomeElements.TITLE_9,
            description = TestHomeElements.DESCRIPTION_9,
            url = TestHomeElements.URL_9,
        ),
    )

    private val coverDtos = listOf(
        CoverDto(
            id = TestHomeElements.ID_1,
            year = TestHomeElements.YEAR_23,
            country = TestHomeElements.COUNTRY_RUSSIA,
            place = TestHomeElements.PLACE_MOSCOW,
            title = TestHomeElements.TITLE_1,
            description = TestHomeElements.DESCRIPTION_1,
            imageUrl = TestHomeElements.URL_1,
        ),
        CoverDto(
            id = TestHomeElements.ID_2,
            year = TestHomeElements.YEAR_23,
            country = TestHomeElements.COUNTRY_RUSSIA,
            place = TestHomeElements.PLACE_KARELIA,
            title = TestHomeElements.TITLE_2,
            description = TestHomeElements.DESCRIPTION_2,
            imageUrl = TestHomeElements.URL_2,
        ),
        CoverDto(
            id = TestHomeElements.ID_7,
            year = TestHomeElements.YEAR_23,
            country = TestHomeElements.COUNTRY_TURKEY,
            place = TestHomeElements.PLACE_CAPPADOCIA,
            title = TestHomeElements.TITLE_7,
            description = TestHomeElements.DESCRIPTION_7,
            imageUrl = TestHomeElements.URL_7,
        ),
        CoverDto(
            id = TestHomeElements.ID_8,
            year = TestHomeElements.YEAR_24,
            country = TestHomeElements.COUNTRY_RUSSIA,
            place = TestHomeElements.PLACE_MURMANSK,
            title = TestHomeElements.TITLE_8,
            description = TestHomeElements.DESCRIPTION_8,
            imageUrl = TestHomeElements.URL_8,
        ),
        CoverDto(
            id = TestHomeElements.ID_9,
            year = TestHomeElements.YEAR_24,
            country = TestHomeElements.COUNTRY_RUSSIA,
            place = TestHomeElements.PLACE_MOSCOW,
            title = TestHomeElements.TITLE_9,
            description = TestHomeElements.DESCRIPTION_9,
            imageUrl = TestHomeElements.URL_9,
        ),
    )

    private val contentDto = ContentDto(
        id = TestHomeElements.CONTENT_ID_1,
        coverId = TestHomeElements.ID_1,
        title = TestHomeElements.MOSCOW_CONTENT_TITLE,
        body = TestHomeElements.MOSCOW_CONTENT_TEXT,
        imagesUrls = listOf(TestHomeElements.URL_1, TestHomeElements.URL_2),
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        homeElementDao = mockk()
        homeDatabase = mockk {
            every { homeElementDao() } returns homeElementDao
        }
        every { homeElementDao.getAll() } returns flow {
            delay(100)
            emit(coverEntities)
        }
        coEvery { homeElementDao.insertAll(any()) } just Runs

        serverApi = mockk(relaxed = true)
        coEvery { serverApi.getCovers() } coAnswers {
            delay(200)
            coverDtos
        }
        coEvery { serverApi.getContent( TestHomeElements.CONTENT_ID_1) } coAnswers {
            delay(100)
            contentDto
        }
        serverRepository = ServerRepository(
            serverApi = serverApi,
        )

        dataRepository = DataRepository(
            homeDatabase = homeDatabase,
            serverRepository = serverRepository,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getHomeElements returns InProgress then Result`() = runTest {
        val result = dataRepository.getHomeElements().toList()

        val resultInProgress1 = result[0]

        assert(resultInProgress1 is RequestResult.InProgress)
        assertEquals(null, resultInProgress1.data)

        val resultInProgress2 = result[1]

        assert(resultInProgress2 is RequestResult.InProgress)
        val resultInProgressData = resultInProgress2.data.orEmpty()
        assertEquals(3, resultInProgressData.size)
        assertEquals(TestHomeElements.ID_1, resultInProgressData[0].id)
        assertEquals(TestHomeElements.ID_7, resultInProgressData[1].id)
        assertEquals(TestHomeElements.ID_9, resultInProgressData[2].id)

        val resultSuccess = result[2]

        assert(resultSuccess is RequestResult.Success)
        val resultSuccessData = resultSuccess.data.orEmpty()
        assertEquals(5, resultSuccessData.size)
        assertEquals(TestHomeElements.ID_1, resultSuccessData[0].id)
        assertEquals(TestHomeElements.ID_2, resultSuccessData[1].id)
        assertEquals(TestHomeElements.ID_7, resultSuccessData[2].id)
        assertEquals(TestHomeElements.ID_8, resultSuccessData[3].id)
        assertEquals(TestHomeElements.ID_9, resultSuccessData[4].id)
    }

    @Test
    fun `getHomeElementsFromDatabase returns InProgress then Result`() = runTest {
        val result = dataRepository.getHomeElementsFromDatabase(
            tabFilter = HomeFilterType.COUNTRY,
            selectorFilter = null,
        ).toList()

        val resultInProgress = result[0]

        assert(resultInProgress is RequestResult.InProgress)
        assertEquals(null, resultInProgress.data)

        val resultSuccess = result[1]

        assert(resultSuccess is RequestResult.Success)
        val resultData = resultSuccess.data.orEmpty()
        assertEquals(2, resultData.size)
        assertEquals(TestHomeElements.ID_1, resultData[0].id)
        assertEquals(TestHomeElements.ID_7, resultData[1].id)
    }

    @Test
    fun `getContent returns InProgress then Result`() = runTest {
        val result = dataRepository.getContent(TestHomeElements.CONTENT_ID_1).toList()

        val resultInProgress = result[0]
        assert(resultInProgress is RequestResult.InProgress)
        assertEquals(null, resultInProgress.data)

        val resultSuccess = result[1]
        assert(resultSuccess is RequestResult.Success)

        val resultData = (resultSuccess as RequestResult.Success).data
        assertNotNull(resultData)
        assertEquals(TestHomeElements.CONTENT_ID_1, resultData.id)
    }

    @Test
    fun `getHomeElementsFromDatabase filters by tabFilter`() = runTest {
        val result = dataRepository.getHomeElementsFromDatabase(
            tabFilter = HomeFilterType.COUNTRY,
            selectorFilter = null,
        ).filter {
            it is RequestResult.Success
        }

        val resultList = (result.first() as RequestResult.Success).data

        assertEquals(2, resultList.size)
        assertEquals(TestHomeElements.ID_1, resultList[0].id)
        assertEquals(TestHomeElements.ID_7, resultList[1].id)
    }

    @Test
    fun `getHomeElementsFromDatabase filters by selectorFilter`() = runTest {
        val result = dataRepository.getHomeElementsFromDatabase(
            tabFilter = null,
            selectorFilter = SelectorFilterType.PLACE.apply { value =
                TestHomeElements.PLACE_MOSCOW
            },
        ).filter {
            it is RequestResult.Success
        }

        val resultList = (result.first() as RequestResult.Success).data

        assertEquals(2, resultList.size)
        assertEquals(TestHomeElements.ID_1, resultList[0].id)
        assertEquals(TestHomeElements.ID_9, resultList[1].id)
    }
}