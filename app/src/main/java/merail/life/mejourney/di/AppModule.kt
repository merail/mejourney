package merail.life.mejourney.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import merail.life.mejourney.auth.FirebaseAuthRepository
import merail.life.mejourney.auth.IFirebaseAuthRepository
import merail.life.mejourney.data.FirebaseRepository
import merail.life.mejourney.data.IFirebaseRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    @Singleton
    fun bindsFirebaseAuthRepository(
        firebaseAuthRepository: FirebaseAuthRepository,
    ): IFirebaseAuthRepository

    @Binds
    @Singleton
    fun bindsFirebaseRepository(
        firebaseRepository: FirebaseRepository,
    ): IFirebaseRepository
}