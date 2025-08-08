package merail.life.data.impl.di

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
import merail.life.data.api.IDataRepository
import merail.life.data.impl.DataRepository
import merail.life.data.impl.database.HOME_DATABASE_NAME
import merail.life.data.impl.database.HomeDatabase
import merail.life.data.impl.server.IServerRepository
import merail.life.data.impl.server.ServerRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    @Binds
    @Singleton
    fun bindDataRepository(
        dataRepository: DataRepository,
    ): IDataRepository

    @Binds
    @Singleton
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
            context = context,
            klass = HomeDatabase::class.java,
            name = HOME_DATABASE_NAME,
        ).build()
    }
}