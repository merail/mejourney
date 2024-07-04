package merail.life.data

import kotlinx.coroutines.flow.Flow
import merail.life.core.RequestResult
import merail.life.data.model.ContentModel
import merail.life.data.model.HomeElementModel
import merail.life.data.model.HomeFilterType
import merail.life.data.model.SelectorFilterModel

interface IDataRepository {

    fun getHomeElements(): Flow<RequestResult<List<HomeElementModel>>>

    fun getHomeElementsFromDatabase(
        tabFilter: HomeFilterType? = null,
        selectorFilter: SelectorFilterModel? = null,
    ): Flow<RequestResult<List<HomeElementModel>>>

    fun getContent(
        id: String,
    ): Flow<RequestResult<ContentModel>>
}
