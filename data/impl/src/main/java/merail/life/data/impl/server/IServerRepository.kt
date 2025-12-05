package merail.life.data.impl.server

import merail.life.data.impl.server.dto.FirestoreDto
import merail.life.data.impl.server.dto.StorageDto

internal interface IServerRepository {

    suspend fun getFirestoreData(
        folderName: String,
    ): FirestoreDto

    suspend fun getStorageData(
        folderName: String,
    ): List<StorageDto>
}
