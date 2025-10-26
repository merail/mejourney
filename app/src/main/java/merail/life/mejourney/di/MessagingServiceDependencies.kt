package merail.life.mejourney.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import merail.life.core.log.IMejourneyLogger

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface MessagingServiceDependencies {
    fun logger(): IMejourneyLogger
}