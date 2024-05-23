package merail.life.mejourney.data

interface IFirebaseRepository {
    suspend fun getHomeItems(
        filter: TabFilter = TabFilter.COMMON,
    ): List<HomeItem>
}
