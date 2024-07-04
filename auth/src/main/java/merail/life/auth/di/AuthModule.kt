package merail.life.auth.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import merail.life.auth.api.FirebaseAuthRepository
import merail.life.auth.api.IFirebaseAuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {
    @Singleton
    @Binds
    fun bindFirebaseAuthRepository(
        firebaseAuthRepository: FirebaseAuthRepository,
    ): IFirebaseAuthRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth
    }
}