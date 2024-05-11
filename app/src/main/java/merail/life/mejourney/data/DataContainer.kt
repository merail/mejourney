package merail.life.mejourney.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage

interface DataContainer {
    val firebaseRepository: IFirebaseRepository
}

class FirebaseDataContainer : DataContainer {
    override val firebaseRepository: IFirebaseRepository by lazy {
        FirebaseRepository(
            firebaseStorage = Firebase.storage,
            firebaseFirestore = Firebase.firestore,
        )
    }
}
