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

        private const val FOLDER_NAME = "main"
    }

    private val storageReference = firebaseStorage.getReferenceFromUrl(BUCKET_REFERENCE)

    private val mainFolderReference = storageReference.child(FOLDER_NAME)

    override suspend fun getUrl(
        fileName: String,
    ): HomeItem {
        val fileReference = mainFolderReference.child(fileName)
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