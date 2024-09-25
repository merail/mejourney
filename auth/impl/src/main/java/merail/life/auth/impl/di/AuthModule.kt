package merail.life.auth.impl.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import merail.life.auth.api.IAuthRepository
import merail.life.auth.impl.repository.AuthRepository
import merail.life.auth.impl.repository.EmailSender
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface AuthModule {
    @Singleton
    @Binds
    fun bindFirebaseRepository(
        firebaseRepository: AuthRepository,
    ): IAuthRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

        @Provides
        @Singleton
        fun provideEmailSender(
            @ApplicationContext context: Context
        ): EmailSender = EmailSender(context)
    }
}