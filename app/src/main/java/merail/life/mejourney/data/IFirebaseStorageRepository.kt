package merail.life.mejourney.data

interface IFirebaseStorageRepository {
    suspend fun getUrl(
        fileName: String,
    ) : HomeItem
}
