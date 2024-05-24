package merail.life.mejourney.data

import merail.life.mejourney.data.model.ContentItem
import merail.life.mejourney.data.model.HomeItem
import merail.life.mejourney.data.model.TabFilter

interface IFirebaseRepository {

    suspend fun getHomeItems(
        filter: TabFilter = TabFilter.COMMON,
    ): List<HomeItem>

    suspend fun getContentItem(
        id: String,
    ): ContentItem
}
