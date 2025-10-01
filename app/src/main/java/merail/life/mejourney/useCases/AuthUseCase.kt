package merail.life.mejourney.useCases

import merail.life.auth.api.IAuthRepository
import merail.life.core.extensions.suspendableRunCatching
import merail.life.core.log.IMejourneyLogger
import merail.life.mejourney.MainViewModel.Companion.TAG
import merail.life.mejourney.state.MainAuthState
import javax.inject.Inject

internal class AuthUseCase @Inject constructor(
    private val authRepository: IAuthRepository,
    private val logger: IMejourneyLogger,
) {
    suspend operator fun invoke() = suspendableRunCatching {
        logger.d(TAG, "Авторизация. Старт")
        authRepository.authorize()
    }.fold(
        onSuccess = {
            logger.d(TAG, "Авторизация. Успех")
            MainAuthState.AuthSuccess
        },
        onFailure = { throwable ->
            logger.w(TAG, "Авторизация. Ошибка", throwable)
            MainAuthState.Error(throwable)
        },
    )
}