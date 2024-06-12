package merail.life.firebase.data

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.tasks.await
import merail.life.firebase.BuildConfig
import merail.life.firebase.data.dto.ContentFirestoreDto
import merail.life.firebase.data.dto.CoversFirestoreDto
import merail.life.firebase.data.dto.toDto
import merail.life.firebase.data.model.ContentModel
import merail.life.firebase.data.model.HomeFilterType
import merail.life.firebase.data.model.HomeModel
import merail.life.firebase.data.model.SelectorFilterModel
import javax.inject.Inject

class FirebaseRepository @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseFirestore: FirebaseFirestore,
) : IFirebaseRepository {

    companion object {
        private const val TAG = "FirebaseRepository"

        private const val MAIN_PATH = BuildConfig.FIREBASE_REPOSITORY_PATH

        private const val HOME_COVERS_PATH = "$MAIN_PATH/home_covers"

        private const val CONTENT_PATH = "$MAIN_PATH/"

        private const val BUCKET_REFERENCE = BuildConfig.FIREBASE_STORAGE_BUCKET
    }

    private val mutex = Mutex()

    private val items = mutableListOf<HomeModel>()

    override suspend fun getHomeItems(
        filter: HomeFilterType,
    ): List<HomeModel> {
        if (items.isNotEmpty()) {
            return items.filter(filter)
        }
        if (mutex.isLocked.not()) {
            mutex.lock()
            val firestoreData = CoversFirestoreDto(getFirestoreData(HOME_COVERS_PATH))
            val storageData = getStorageData(HOME_COVERS_PATH)
            val newItems = firestoreData.toDto().zip(storageData).map { (info, file) ->
                HomeModel(
                    id = info.id,
                    year = info.year,
                    country = info.country,
                    place = info.place,
                    title = info.title,
                    description = info.description,
                    url = file.getUrl(),
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

    override suspend fun getHomeItems(
        tabFilter: HomeFilterType,
        selectorFilter: SelectorFilterModel
    ) = when (selectorFilter) {
        is SelectorFilterModel.Year -> items.filter {
            it.year == selectorFilter.year
        }
        is SelectorFilterModel.Country ->  items.filter {
            it.country == selectorFilter.country
        }
        is SelectorFilterModel.Place -> items.filter {
            it.place == selectorFilter.place
        }
    }

    override suspend fun getContentItem(
        id: String,
    ): ContentModel {
        val firestoreData = ContentFirestoreDto(getFirestoreData("$CONTENT_PATH$id"))
        val storageData = getStorageData("$CONTENT_PATH$id")
        val contentDto = firestoreData.toDto()
        val imagesUrls = storageData.map { file ->
            file.getUrl()
        }
        return ContentModel(
            title = contentDto.title,
            text = contentDto.text,
            imagesUrls = imagesUrls.toImmutableList(),
        )
    }

    private fun MutableList<HomeModel>.filter(
        filter: HomeFilterType,
    ) = when (filter) {
        HomeFilterType.YEAR -> groupBy {
            it.year
        }.values.map {
            it[0]
        }
        HomeFilterType.PLACE -> groupBy {
            it.place
        }.values.map {
            it[0]
        }
        HomeFilterType.COUNTRY -> groupBy {
            it.country
        }.values.map {
            it[0]
        }
        HomeFilterType.COMMON -> this
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

    private suspend fun StorageReference.getUrl() = downloadUrl
        .addOnFailureListener {
            Log.w(TAG, "Getting $name url from Firebase Storage. Failure")
        }
        .addOnSuccessListener {
            Log.d(TAG, "Getting $name url from Firebase Storage. Success: $it")
        }
        .await()
        .toString()
}