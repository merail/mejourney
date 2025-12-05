package merail.life.home.main.useCases

import merail.life.auth.api.IAuthRepository
import merail.life.core.extensions.suspendableRunCatching
import merail.life.core.log.IMejourneyLogger
import merail.life.home.main.HomeViewModel
import javax.inject.Inject

internal class LoadSnowfallStateUseCase @Inject constructor(
    private val authRepository: IAuthRepository,
    private val logger: IMejourneyLogger,
) {
    suspend operator fun invoke() = suspendableRunCatching {
        logger.d(HomeViewModel.TAG, "Snowfall state fetch. Start")
        authRepository.isSnowfallEnabled()
    }.fold(
        onSuccess = {
            logger.d(HomeViewModel.TAG, "Snowfall state fetch. Success")
            true
        },
        onFailure = { throwable ->
            logger.w(HomeViewModel.TAG, "Snowfall state fetch. Failure", throwable)
            false
        },
    )
}