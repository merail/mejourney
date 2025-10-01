package merail.life.home.main.useCases

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import merail.life.core.log.IMejourneyLogger
import merail.life.core.mappers.RequestResult
import merail.life.data.api.IDataRepository
import merail.life.data.api.model.HomeElementModel
import merail.life.home.main.HomeViewModel.Companion.TAG
import merail.life.home.main.toState
import javax.inject.Inject

internal class LoadHomeElementsUseCase @Inject constructor(
    private val dataRepository: IDataRepository,
    private val logger: IMejourneyLogger,
) {
    operator fun invoke() = dataRepository.getHomeElements().onEach {
        logger.d(TAG, "Получение списка элементов. $it")
    }.map(RequestResult<List<HomeElementModel>>::toState)
}