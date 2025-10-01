package merail.life.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import merail.life.core.log.IMejourneyLogger
import merail.life.core.log.MejourneyLogger
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LoggingModule {
    @Binds
    @Singleton
    abstract fun bindLogger(logger: MejourneyLogger): IMejourneyLogger
}