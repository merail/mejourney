package merail.life.firebase.di

import android.content.Context
import androidx.room.Room
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
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import merail.life.firebase.auth.FirebaseAuthRepository
import merail.life.firebase.auth.IFirebaseAuthRepository
import merail.life.firebase.data.FirebaseRepository
import merail.life.firebase.data.IFirebaseRepository
import merail.life.firebase.data.IInternalFirebaseRepository
import merail.life.firebase.data.InternalFirebaseRepository
import merail.life.firebase.data.database.HOME_DATABASE_NAME
import merail.life.firebase.data.database.HomeDatabase
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

    @Singleton
    @Binds
    fun bindsInternalFirebaseRepository(
        internalFirebaseRepository: InternalFirebaseRepository,
    ): IInternalFirebaseRepository

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

        @Provides
        @Singleton
        fun provideHomeDatabase(
            @ApplicationContext context: Context,
        ): HomeDatabase = Room.databaseBuilder(
            context,
            HomeDatabase::class.java,
            HOME_DATABASE_NAME,
        ).build()
    }
}