package merail.life.data.impl

import android.net.Uri
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
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
import merail.life.core.constants.HomeElementsFields
import merail.life.core.constants.TestHomeElements
import merail.life.core.mappers.RequestResult
import merail.life.data.api.IDataRepository
import merail.life.data.api.model.HomeFilterType
import merail.life.data.api.model.SelectorFilterType
import merail.life.data.impl.database.HomeDatabase
import merail.life.data.impl.database.HomeElementDao
import merail.life.data.impl.database.dto.HomeElementEntity
import merail.life.data.impl.server.ServerRepository
import merail.life.data.impl.server.dto.FirestoreDto
import merail.life.data.impl.server.dto.StorageDto
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DataRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var homeDatabase: HomeDatabase
    private lateinit var homeElementDao: HomeElementDao
    private lateinit var serverRepository: ServerRepository
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

    private val mockedFirestoreDto = let {
        val mockDocumentSnapshot1 = mockk<QueryDocumentSnapshot> {
            every { id } returns TestHomeElements.ID_1
            every { data } returns mapOf(
                HomeElementsFields.YEAR to TestHomeElements.YEAR_23,
                HomeElementsFields.COUNTRY to TestHomeElements.COUNTRY_RUSSIA,
                HomeElementsFields.PLACE to TestHomeElements.PLACE_MOSCOW,
                HomeElementsFields.TITLE to TestHomeElements.TITLE_1,
                HomeElementsFields.DESCRIPTION to TestHomeElements.DESCRIPTION_1,
            )
        }

        val mockDocumentSnapshot2 = mockk<QueryDocumentSnapshot> {
            every { id } returns TestHomeElements.ID_2
            every { data } returns mapOf(
                HomeElementsFields.YEAR to TestHomeElements.YEAR_23,
                HomeElementsFields.COUNTRY to TestHomeElements.COUNTRY_RUSSIA,
                HomeElementsFields.PLACE to TestHomeElements.PLACE_KARELIA,
                HomeElementsFields.TITLE to TestHomeElements.TITLE_2,
                HomeElementsFields.DESCRIPTION to TestHomeElements.DESCRIPTION_2,
            )
        }

        val mockDocumentSnapshot7 = mockk<QueryDocumentSnapshot> {
            every { id } returns TestHomeElements.ID_7
            every { data } returns mapOf(
                HomeElementsFields.YEAR to TestHomeElements.YEAR_23,
                HomeElementsFields.COUNTRY to TestHomeElements.COUNTRY_TURKEY,
                HomeElementsFields.PLACE to TestHomeElements.PLACE_CAPPADOCIA,
                HomeElementsFields.TITLE to TestHomeElements.TITLE_7,
                HomeElementsFields.DESCRIPTION to TestHomeElements.DESCRIPTION_7,
            )
        }

        val mockDocumentSnapshot8 = mockk<QueryDocumentSnapshot> {
            every { id } returns TestHomeElements.ID_8
            every { data } returns mapOf(
                HomeElementsFields.YEAR to TestHomeElements.YEAR_24,
                HomeElementsFields.COUNTRY to TestHomeElements.COUNTRY_RUSSIA,
                HomeElementsFields.PLACE to TestHomeElements.PLACE_MURMANSK,
                HomeElementsFields.TITLE to TestHomeElements.TITLE_8,
                HomeElementsFields.DESCRIPTION to TestHomeElements.DESCRIPTION_8,
            )
        }

        val mockDocumentSnapshot9 = mockk<QueryDocumentSnapshot> {
            every { id } returns TestHomeElements.ID_9
            every { data } returns mapOf(
                HomeElementsFields.YEAR to TestHomeElements.YEAR_24,
                HomeElementsFields.COUNTRY to TestHomeElements.COUNTRY_RUSSIA,
                HomeElementsFields.PLACE to TestHomeElements.PLACE_MOSCOW,
                HomeElementsFields.TITLE to TestHomeElements.TITLE_9,
                HomeElementsFields.DESCRIPTION to TestHomeElements.DESCRIPTION_9,
            )
        }

        val mockQuerySnapshot = mockk<QuerySnapshot> {
            every {
                iterator()
            } returns mutableListOf(
                mockDocumentSnapshot1,
                mockDocumentSnapshot2,
                mockDocumentSnapshot7,
                mockDocumentSnapshot8,
                mockDocumentSnapshot9,
            ).iterator()
        }

        FirestoreDto(mockQuerySnapshot)
    }

    private val mockedStorageDtos = let {
        val mockUri1 = mockk<Uri> {
            every { this@mockk.toString() } returns TestHomeElements.URL_1
        }
        val mockUri2 = mockk<Uri> {
            every { this@mockk.toString() } returns TestHomeElements.URL_2
        }
        val mockUri7 = mockk<Uri> {
            every { this@mockk.toString() } returns TestHomeElements.URL_7
        }
        val mockUri8 = mockk<Uri> {
            every { this@mockk.toString() } returns TestHomeElements.URL_8
        }
        val mockUri9 = mockk<Uri> {
            every { this@mockk.toString() } returns TestHomeElements.URL_9
        }

        listOf(
            StorageDto(mockUri1),
            StorageDto(mockUri2),
            StorageDto(mockUri7),
            StorageDto(mockUri8),
            StorageDto(mockUri9),
        )
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        homeElementDao = mockk()
        homeDatabase = mockk {
            every { homeElementDao() } returns homeElementDao
        }
        every { homeElementDao.getAll() } returns flow {
            delay(100)
            emit(entities)
        }
        coEvery { homeElementDao.insertAll(any()) } just Runs

        serverRepository = mockk()
        coEvery { serverRepository.getFirestoreData(any()) } coAnswers {
            delay(100)
            mockedFirestoreDto
        }
        coEvery { serverRepository.getStorageData(any()) } coAnswers {
            delay(100)
            mockedStorageDtos
        }

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