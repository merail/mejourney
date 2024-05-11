package merail.life.mejourney.data

interface IFirebaseRepository {
    suspend fun getHomeItems(): List<HomeItem>
}
