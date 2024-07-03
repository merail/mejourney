package merail.life.api.data

import merail.life.api.data.model.FirestoreDto
import merail.life.api.data.model.StorageDto

interface IDataRepository {

    suspend fun getFirestoreData(
        folderName: String,
    ): FirestoreDto

    suspend fun getStorageData(
        folderName: String,
    ): List<StorageDto>
}
