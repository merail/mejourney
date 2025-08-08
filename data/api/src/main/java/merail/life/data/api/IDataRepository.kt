package merail.life.data.api

import kotlinx.coroutines.flow.Flow
import merail.life.core.mappers.RequestResult
import merail.life.data.api.model.ContentModel
import merail.life.data.api.model.HomeElementModel
import merail.life.data.api.model.HomeFilterType
import merail.life.data.api.model.SelectorFilterType

interface IDataRepository {

    fun getHomeElements(): Flow<RequestResult<List<HomeElementModel>>>

    fun getHomeElementsFromDatabase(
        tabFilter: HomeFilterType? = null,
        selectorFilter: SelectorFilterType? = null,
    ): Flow<RequestResult<List<HomeElementModel>>>

    fun getContent(
        id: String,
    ): Flow<RequestResult<ContentModel>>
}
