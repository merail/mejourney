package merail.life.mejourney.data

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.tasks.await

class FirebaseRepository(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseFirestore: FirebaseFirestore,
) : IFirebaseRepository {

    companion object {
        private const val TAG = "FirebaseRepository"

        private const val MAIN_PATH = "dev"

        private const val HOME_COVERS_PATH = "$MAIN_PATH/home_covers"

        private const val BUCKET_REFERENCE = "gs://mejourney-c86ca.appspot.com"
    }

    private val mutex = Mutex()

    private val items = mutableListOf<HomeItem>()

    override suspend fun getHomeItems(
        filter: TabFilter,
    ): List<HomeItem> {
        if (items.isNotEmpty()) {
            return items.filter(filter)
        }
        if (mutex.isLocked.not()) {
            mutex.lock()
            val firestoreData = getFirestoreData(HOME_COVERS_PATH)
            val storageData = getStorageData(HOME_COVERS_PATH)
            val newItems = firestoreData.zip(storageData).map { (info, file) ->
                HomeItem(
                    year = info.getCoverData().year,
                    country = info.getCoverData().country,
                    place = info.getCoverData().place,
                    title = info.getCoverData().title,
                    description = info.getCoverData().description,
                    url = file.getCoverUrl(),
                )
            }
            items.clear()
            items.addAll(newItems)
            mutex.unlock()
        } else {
            mutex.lock()
            mutex.unlock()
        }
        return items.filter(filter)
    }

    private fun MutableList<HomeItem>.filter(
        filter: TabFilter,
    ) = when (filter) {
        TabFilter.YEAR -> groupBy {
            it.year
        }.values.map {
            it[0]
        }
        TabFilter.PLACE -> groupBy {
            it.place
        }.values.map {
            it[0]
        }
        TabFilter.COUNTRY -> groupBy {
            it.country
        }.values.map {
            it[0]
        }
        TabFilter.COMMON -> this
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

    private fun QueryDocumentSnapshot.getCoverData() = CoverData(
        year = data["year"] as Long,
        country = data["country"] as String,
        place = data["place"] as String,
        title = data["title"] as String,
        description = data["description"] as String,
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