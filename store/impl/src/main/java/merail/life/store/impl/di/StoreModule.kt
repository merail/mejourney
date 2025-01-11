package merail.life.store.impl.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import merail.life.store.api.IStoreRepository
import merail.life.store.impl.repository.StoreRepository
import merail.life.store.impl.repository.dataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface StoreModule {
    @Singleton
    @Binds
    fun bindStoreRepository(
        storeRepository: StoreRepository,
    ): IStoreRepository

    companion object {
        @Provides
        @Singleton
        fun provideDataStore(
            @ApplicationContext context: Context,
        ): DataStore<Preferences> = context.dataStore
    }
}