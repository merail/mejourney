package merail.life.firebase.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import merail.life.firebase.auth.FirebaseAuthRepository
import merail.life.firebase.auth.IFirebaseAuthRepository
import merail.life.firebase.data.FirebaseRepository
import merail.life.firebase.data.IFirebaseRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface FirebaseModule {

    @Singleton
    @Binds
    fun bindsFirebaseAuthRepository(
        firebaseAuthRepository: FirebaseAuthRepository,
    ): IFirebaseAuthRepository

    @Singleton
    @Binds
    fun bindsFirebaseRepository(
        firebaseRepository: FirebaseRepository,
    ): IFirebaseRepository

    companion object {

        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

        @Provides
        @Singleton
        fun provideFirebaseStorage(): FirebaseStorage = Firebase.storage

        @Provides
        @Singleton
        fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore
    }
}