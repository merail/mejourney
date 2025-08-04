package merail.life.auth.impl.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import merail.life.auth.api.IAuthRepository
import merail.life.auth.impl.repository.AuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface AuthModule {
    @Singleton
    @Binds
    fun bindAuthRepository(
        authRepository: AuthRepository,
    ): IAuthRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

        @Provides
        @Singleton
        fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig = Firebase.remoteConfig
    }
}