package merail.life.data.test.repository

import kotlinx.coroutines.flow.flowOf
import merail.life.core.constants.TestHomeElements
import merail.life.core.mappers.RequestResult
import merail.life.data.api.IDataRepository
import merail.life.data.api.model.ContentModel
import merail.life.data.api.model.HomeElementModel
import merail.life.data.api.model.HomeFilterType
import merail.life.data.api.model.SelectorFilterType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestDataRepository @Inject constructor(): IDataRepository {

    private var shouldReturnErrorInGetHomeElements = false

    private var shouldReturnErrorInGetHomeElementsFromDatabase = false

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

    fun setShouldReturnErrorInGetHomeElements(value: Boolean) {
        shouldReturnErrorInGetHomeElements = value
    }

    fun setShouldReturnErrorInGetHomeElementsFromDatabase(value: Boolean) {
        shouldReturnErrorInGetHomeElementsFromDatabase = value
    }

    override fun getHomeElements() = flowOf(
        value = if (shouldReturnErrorInGetHomeElements) {
            RequestResult.Error(
                error = RuntimeException("error"),
            )
        } else {
            RequestResult.Success(elements)
        },
    )

    override fun getHomeElementsFromDatabase(
        tabFilter: HomeFilterType?,
        selectorFilter: SelectorFilterType?
    ) = flowOf(
        value = if (shouldReturnErrorInGetHomeElementsFromDatabase) {
            RequestResult.Error(
                error = RuntimeException("error"),
            )
        } else {
            RequestResult.Success(elements)
        },
    )

    override fun getContent(id: String) = flowOf(
        value = RequestResult.Error<ContentModel>(
            error = RuntimeException("error"),
        ),
    )
}