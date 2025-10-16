package merail.life.home.main.useCases

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import merail.life.core.log.IMejourneyLogger
import merail.life.core.mappers.RequestResult
import merail.life.data.api.IDataRepository
import merail.life.data.api.model.HomeElementModel
import merail.life.home.main.HomeViewModel.Companion.TAG
import merail.life.home.main.toState
import merail.life.home.model.TabFilter
import merail.life.home.model.toModel
import javax.inject.Inject

internal class LoadHomeElementsByTabUseCase @Inject constructor(
    private val dataRepository: IDataRepository,
    private val logger: IMejourneyLogger,
) {
    operator fun invoke(filter: TabFilter) = dataRepository.getHomeElementsFromDatabase(
        tabFilter = filter.toModel(),
    ).onEach {
        logger.d(TAG, "Getting home elements list by $filter. $it")
    }.map(RequestResult<List<HomeElementModel>>::toState)
}