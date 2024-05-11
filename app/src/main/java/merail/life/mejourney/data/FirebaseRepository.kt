package merail.life.mejourney.data

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

class FirebaseRepository(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseFirestore: FirebaseFirestore,
) : IFirebaseRepository {

    companion object {
        private const val TAG = "FirebaseRepository"

        private const val MAIN_PATH = "main"

        private const val HOME_COVERS_PATH = "$MAIN_PATH/home_covers"

        private const val BUCKET_REFERENCE = "gs://mejourney-c86ca.appspot.com"
    }

    override suspend fun getHomeItems(): List<HomeItem> {
        val firestoreData = getFirestoreData(HOME_COVERS_PATH)
        val storageData = getStorageData(HOME_COVERS_PATH)
        return firestoreData.zip(storageData).map { (info, file) ->
            HomeItem(
                year = info.getCoverData().first,
                title = info.getCoverData().second,
                description = info.getCoverData().third,
                url = file.getCoverUrl(),
            )
        }
    }

    private suspend fun getFirestoreData(
        folderName: String,
    ) = firebaseFirestore
        .getCollectionFromPath(folderName)
        .get()
        .addOnFailureListener {
            Log.w(TAG, "Getting $folderName folder from Firebase Firestore. Failure")
        }
        .addOnSuccessListener {
            Log.d(TAG, "Getting $folderName folder from Firebase Firestore. Success")
        }
        .await()

    private suspend fun getStorageData(
        folderName: String,
    ) = firebaseStorage
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

    private fun QueryDocumentSnapshot.getCoverData() = Triple(
        first = data["year"] as Long,
        second = data["title"] as String,
        third = data["description"] as String,
    )

    private suspend fun StorageReference.getCoverUrl() = downloadUrl
        .addOnFailureListener {
            Log.w(TAG, "Getting $name url from Firebase Storage. Failure")
        }
        .addOnSuccessListener {
            Log.d(TAG, "Getting $name url from Firebase Storage. Success: $it")
        }
        .await()
        .toString()
}