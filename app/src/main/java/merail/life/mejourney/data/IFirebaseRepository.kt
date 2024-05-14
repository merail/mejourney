package merail.life.mejourney.data

import merail.life.mejourney.ui.home.TabFilter

interface IFirebaseRepository {
    suspend fun getHomeItems(
        filter: TabFilter = TabFilter.ALL,
    ): List<HomeItem>
}
