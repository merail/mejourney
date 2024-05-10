package merail.life.mejourney.auth

import com.google.firebase.Firebase
import com.google.firebase.auth.auth

interface AuthContainer {
    val firebaseAuthRepository: IFirebaseAuthRepository
}

class FirebaseAuthContainer : AuthContainer {
    override val firebaseAuthRepository: IFirebaseAuthRepository by lazy {
        FirebaseAuthRepository(
            firebaseAuth = Firebase.auth,
        )
    }
}
