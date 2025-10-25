package merail.life.data.test.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import merail.life.data.api.IDataRepository
import merail.life.data.test.repository.TestDataRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TestDataModule {
    @Binds
    @Singleton
    fun bindDataRepository(
        dataRepository: TestDataRepository,
    ): IDataRepository
}