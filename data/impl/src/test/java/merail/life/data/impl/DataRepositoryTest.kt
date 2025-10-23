package merail.life.data.impl

import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DataRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var homeDatabase: HomeDatabase
    private lateinit var homeElementDao: HomeElementDao
    private lateinit var dataRepository: IDataRepository

    private val entities = listOf(
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

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        homeElementDao = mockk()
        homeDatabase = mockk {
            every { homeElementDao() } returns homeElementDao
        }
        every { homeElementDao.getAll() } returns flowOf(entities)

        dataRepository = DataRepository(
            homeDatabase = homeDatabase,
            serverRepository = mockk(),
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
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