package merail.life.data.impl.server

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import merail.life.core.errors.NoInternetConnectionException
import merail.life.core.errors.tryMapToUnauthorizedException
import merail.life.core.extensions.Slash
import merail.life.core.extensions.suspendableRunCatching
import merail.life.data.impl.BuildConfig
import merail.life.data.impl.server.dto.FirestoreDto
import merail.life.data.impl.server.dto.StorageDto
import merail.life.data.impl.server.dto.toDto
import javax.inject.Inject

internal class ServerRepository @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseFirestore: FirebaseFirestore,
) : IServerRepository {

    companion object {

        private const val STORAGE_ROOT = BuildConfig.FIREBASE_REPOSITORY_PATH

        private const val BUCKET_REFERENCE = BuildConfig.FIREBASE_STORAGE_BUCKET
    }

    override suspend fun getFirestoreData(
        folderName: String,
    ): FirestoreDto = withContext(Dispatchers.IO) {
        suspendableRunCatching {
            firebaseFirestore.getCollectionFromPath(folderName).get().await()
        }.onFailure { error ->
            throw error.tryMapToUnauthorizedException()
        }.onSuccess {
            if (it.metadata.isFromCache) {
                throw NoInternetConnectionException()
            }
        }.getOrThrow().toDto()
    }

    override suspend fun getStorageData(
        folderName: String,
    ): List<StorageDto> = withContext(Dispatchers.IO) {
        firebaseStorage
            .getReferenceFromUrl(BUCKET_REFERENCE)
            .child("$STORAGE_ROOT/$folderName")
            .listAll()
            .await()
            .items
            .fetchFilesUris()
            .toDto()
    }

    private fun FirebaseFirestore.getCollectionFromPath(
        path: String,
    ): CollectionReference {
        val pathList = "$STORAGE_ROOT/$path".split(String.Slash)
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