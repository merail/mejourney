package merail.life.firebase.data

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.StorageReference

interface IInternalFirebaseRepository {

    suspend fun getFirestoreData(
        folderName: String,
    ): QuerySnapshot

    suspend fun getStorageData(
        folderName: String,
    ): MutableList<StorageReference>
}
