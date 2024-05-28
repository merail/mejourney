package merail.life.firebase.data

import merail.life.firebase.data.model.ContentModel
import merail.life.firebase.data.model.HomeFilterType
import merail.life.firebase.data.model.HomeModel

interface IFirebaseRepository {

    suspend fun getHomeItems(
        filter: HomeFilterType = HomeFilterType.COMMON,
    ): List<HomeModel>

    suspend fun getContentItem(
        id: String,
    ): ContentModel
}
