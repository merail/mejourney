package merail.life.mejourney.data

interface IFirebaseStorageRepository {

    suspend fun getItems(
        folderName: String,
    ): List<HomeItem>

    suspend fun getItem(
        folderName: String,
        fileName: String,
    ): HomeItem
}
