package merail.life.data.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.Firebase
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
import merail.life.api.data.IServerRepository
import merail.life.api.data.ServerRepository
import merail.life.api.database.HOME_DATABASE_NAME
import merail.life.api.database.HomeDatabase
import merail.life.data.DataRepository
import merail.life.data.IDataRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindDataRepository(
        dataRepository: DataRepository,
    ): IDataRepository

    @Singleton
    @Binds
    fun bindServerRepository(
        serverRepository: ServerRepository,
    ): IServerRepository

    companion object {

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