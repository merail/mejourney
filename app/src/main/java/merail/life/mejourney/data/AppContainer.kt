package merail.life.mejourney.data

import com.google.firebase.Firebase
import com.google.firebase.storage.storage

interface AppContainer {
    val firebaseStorageRepository: FirebaseStorageRepository
}

class AppDataContainer : AppContainer {
    override val firebaseStorageRepository: FirebaseStorageRepository by lazy {
        OfflineFirebaseStorageRepository(Firebase.storage)
    }
}
