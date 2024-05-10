package merail.life.mejourney.data

import com.google.firebase.storage.FirebaseStorage

class OfflineFirebaseStorageRepository(
    private val firebaseStorage: FirebaseStorage,
) : FirebaseStorageRepository