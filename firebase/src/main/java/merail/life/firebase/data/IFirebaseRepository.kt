package merail.life.firebase.data

import merail.life.firebase.data.model.ContentItem
import merail.life.firebase.data.model.HomeModel
import merail.life.firebase.data.model.HomeFilterType

interface IFirebaseRepository {

    suspend fun getHomeItems(
        filter: HomeFilterType = HomeFilterType.COMMON,
    ): List<HomeModel>

    suspend fun getContentItem(
        id: String,
    ): ContentItem
}
