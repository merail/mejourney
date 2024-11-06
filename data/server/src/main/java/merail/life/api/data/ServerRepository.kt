package merail.life.api.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import merail.life.api.data.model.FirestoreDto
import merail.life.api.data.model.StorageDto
import merail.life.api.data.model.toDto
import merail.life.core.NoInternetConnectionException
import merail.life.core.tryMapToUnauthorizedException
import javax.inject.Inject

class ServerRepository @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseFirestore: FirebaseFirestore,
) : IServerRepository {

    companion object {

        private const val STORAGE_ROOT = BuildConfig.FIREBASE_REPOSITORY_PATH

        private const val BUCKET_REFERENCE = BuildConfig.FIREBASE_STORAGE_BUCKET
    }

    override suspend fun getFirestoreData(
        folderName: String,
    ): FirestoreDto = runCatching {
        firebaseFirestore.getCollectionFromPath(folderName).get().await()
    }.onFailure { error ->
        error.tryMapToUnauthorizedException()?.let {
            throw it
        }
    }.onSuccess {
        if (it.metadata.isFromCache) {
            throw NoInternetConnectionException()
        }
    }.getOrThrow().toDto()

    override suspend fun getStorageData(
        folderName: String,
    ): List<StorageDto> = firebaseStorage
        .getReferenceFromUrl(BUCKET_REFERENCE)
        .child("$STORAGE_ROOT/$folderName")
        .listAll()
        .await()
        .items
        .fetchFilesUris()
        .toDto()

    private fun FirebaseFirestore.getCollectionFromPath(
        path: String,
    ): CollectionReference {
        val pathList = "$STORAGE_ROOT/$path".split("/")
        if (pathList.isNotEmpty()) {
            var collection = collection(pathList[0]).document("${pathList[0]}Document")
            for (i in 1 until pathList.size - 1) {
                collection = collection(pathList[i]).document("${pathList[i]}Document")
            }
            return collection.collection(pathList.last())
        }
        throw IllegalStateException("Empty path in Firestore database!")
    }

    private suspend fun List<StorageReference>.fetchFilesUris() = map { reference ->
        reference.downloadUrl.await()
    }
}