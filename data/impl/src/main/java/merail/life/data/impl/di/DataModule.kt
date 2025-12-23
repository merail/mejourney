package merail.life.data.impl.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import merail.life.data.api.IDataRepository
import merail.life.data.api.IServerRepository
import merail.life.data.impl.DataRepository
import merail.life.data.impl.database.HOME_DATABASE_NAME
import merail.life.data.impl.database.HomeDatabase
import merail.life.data.impl.database.MIGRATION_1_2
import merail.life.data.impl.server.ServerRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    internal abstract fun bindDataRepository(
        dataRepository: DataRepository,
    ): IDataRepository

    @Binds
    @Singleton
    internal abstract fun bindServerRepository(
        serverRepository: ServerRepository,
    ): IServerRepository

    companion object {
        @Provides
        @Singleton
        internal fun provideHomeDatabase(
            @ApplicationContext context: Context,
        ): HomeDatabase = Room.databaseBuilder(
            context = context,
            klass = HomeDatabase::class.java,
            name = HOME_DATABASE_NAME,
        ).addMigrations(MIGRATION_1_2).build()
    }
}