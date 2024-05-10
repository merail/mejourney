package merail.life.mejourney.data

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

class FirebaseStorageRepository(
    firebaseStorage: FirebaseStorage,
) : IFirebaseStorageRepository {

    companion object {
        private const val TAG = "FirebaseStorageRepository"

        private const val BUCKET_REFERENCE = "gs://mejourney-c86ca.appspot.com"
    }

    private val storageReference = firebaseStorage.getReferenceFromUrl(BUCKET_REFERENCE)

    override suspend fun getItems(
        folderName: String,
    ): List<HomeItem> = storageReference
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
            getItem(
                folderName = folderName,
                fileName = it.name,
            )
        }


    override suspend fun getItem(
        folderName: String,
        fileName: String,
    ): HomeItem {
        val fileReference = storageReference.child(folderName).child(fileName)
        val url = fileReference.getUrl(fileName)
        return HomeItem(url)
    }

    private suspend fun StorageReference.getUrl(fileName: String) = downloadUrl
        .addOnFailureListener {
            Log.w(TAG, "Getting $fileName url from Firebase Storage. Failure")
        }
        .addOnSuccessListener {
            Log.d(TAG, "Getting $fileName url from Firebase Storage. Success: $it")
        }
        .await()
        .toString()
}