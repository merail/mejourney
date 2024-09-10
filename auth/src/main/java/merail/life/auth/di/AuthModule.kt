package merail.life.auth.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import merail.life.auth.api.AuthRepository
import merail.life.auth.api.IAuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {
    @Singleton
    @Binds
    fun bindFirebaseAuthRepository(
        firebaseAuthRepository: AuthRepository,
    ): IAuthRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth
    }
}