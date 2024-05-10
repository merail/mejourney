package merail.life.mejourney.data

import com.google.firebase.Firebase
import com.google.firebase.storage.storage

interface DataContainer {
    val firebaseStorageRepository: IFirebaseStorageRepository
}

class FirebaseDataContainer : DataContainer {
    override val firebaseStorageRepository: IFirebaseStorageRepository by lazy {
        FirebaseStorageRepository(
            firebaseStorage = Firebase.storage,
        )
    }
}
