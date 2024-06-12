package merail.life.firebase.data

import merail.life.firebase.data.model.ContentModel
import merail.life.firebase.data.model.HomeFilterType
import merail.life.firebase.data.model.HomeModel
import merail.life.firebase.data.model.SelectorFilterModel

interface IFirebaseRepository {

    suspend fun getHomeItems(
        filter: HomeFilterType = HomeFilterType.COMMON,
    ): List<HomeModel>

    suspend fun getHomeItems(
        tabFilter: HomeFilterType,
        selectorFilter: SelectorFilterModel,
    ): List<HomeModel>

    suspend fun getContentItem(
        id: String,
    ): ContentModel
}
