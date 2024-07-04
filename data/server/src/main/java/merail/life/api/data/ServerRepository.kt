package merail.life.api.data

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import merail.life.api.data.model.FirestoreDto
import merail.life.api.data.model.StorageDto
import merail.life.api.data.model.toDto
import javax.inject.Inject

class ServerRepository @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseFirestore: FirebaseFirestore,
) : IServerRepository {
    companion object {
        const val TAG = "ServerRepository"

        private const val BUCKET_REFERENCE = BuildConfig.FIREBASE_STORAGE_BUCKET
    }

    override suspend fun getFirestoreData(
        folderName: String,
    ): FirestoreDto = firebaseFirestore
        .getCollectionFromPath(folderName)
        .get()
        .addOnFailureListener {
            Log.w(TAG, "Getting $folderName folder from Firebase Firestore. Failure")
        }
        .addOnSuccessListener {
            Log.d(TAG, "Getting $folderName folder from Firebase Firestore. Success")
        }
        .await()
        .toDto()

    override suspend fun getStorageData(
        folderName: String,
    ): List<StorageDto> = firebaseStorage
        .getReferenceFromUrl(BUCKET_REFERENCE)
        .child(folderName)
        .listAll()
        .addOnFailureListener {
            Log.w(TAG, "Getting $folderName folder from Firebase Storage. Failure")
        }
        .addOnSuccessListener {
            Log.d(TAG, "Getting $folderName folder from Firebase Storage. Success")
        }
        .await()
        .items
        .map {
            it.toDto()
        }

    private fun FirebaseFirestore.getCollectionFromPath(
        path: String,
    ): CollectionReference {
        val pathList = path.split("/")
        if (pathList.isNotEmpty()) {
            var collection = collection(pathList[0]).document("${pathList[0]}Document")
            for (i in 1 until pathList.size - 1) {
                collection = collection(pathList[i]).document("${pathList[i]}Document")
            }
            return collection.collection(pathList.last())
        }
        throw IllegalStateException("Empty path in Firestore database!")
    }
}