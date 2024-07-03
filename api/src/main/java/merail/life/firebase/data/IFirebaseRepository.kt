package merail.life.firebase.data

import kotlinx.coroutines.flow.Flow
import merail.life.firebase.data.model.ContentModel
import merail.life.firebase.data.model.HomeElementModel
import merail.life.firebase.data.model.HomeFilterType
import merail.life.firebase.data.model.SelectorFilterModel

interface IFirebaseRepository {

    fun getHomeElements(): Flow<RequestResult<List<HomeElementModel>>>

    fun getHomeElementsFromDatabase(
        tabFilter: HomeFilterType? = null,
        selectorFilter: SelectorFilterModel? = null,
    ): Flow<RequestResult<List<HomeElementModel>>>

    suspend fun getContent(
        id: String,
    ): ContentModel
}
