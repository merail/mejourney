package merail.life.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import merail.life.core.extensions.flowWithResult
import merail.life.core.mappers.MergeStrategy
import merail.life.core.mappers.RequestResponseMergeStrategy
import merail.life.core.mappers.RequestResult
import merail.life.core.mappers.toRequestResult
import merail.life.data.api.IDataRepository
import merail.life.data.api.IServerRepository
import merail.life.data.api.model.ContentModel
import merail.life.data.api.model.HomeElementModel
import merail.life.data.api.model.HomeFilterType
import merail.life.data.api.model.SelectorFilterType
import merail.life.data.impl.database.HomeDatabase
import merail.life.data.impl.database.dto.HomeElementEntity
import merail.life.data.impl.database.dto.toEntity
import merail.life.data.impl.database.dto.toModel
import javax.inject.Inject
import kotlin.collections.map

internal class DataRepository @Inject constructor(
    private val homeDatabase: HomeDatabase,
    private val serverRepository: IServerRepository,
) : IDataRepository {

    override fun getHomeElements(): Flow<RequestResult<List<HomeElementModel>>> {
        val mergeStrategy: MergeStrategy<RequestResult<List<HomeElementModel>>> = RequestResponseMergeStrategy()

        val cachedHomeElements = getHomeElementsFromDatabase()
        val remoteHomeElements = getHomeElementsFromServer()

        return cachedHomeElements.combine(remoteHomeElements, mergeStrategy::merge).distinctUntilChanged()
    }

    private fun getHomeElementsFromServer(): Flow<RequestResult<List<HomeElementModel>>> {
        val result = flowWithResult {
            serverRepository.loadCovers()
        }.onEach {
            if (it.isSuccess) {
                saveHomeElementsToDatabase(it.getOrThrow())
            }
        }.map(Result<List<HomeElementModel>>::toRequestResult)

        val start = flowOf<RequestResult<List<HomeElementModel>>>(RequestResult.InProgress())

        return merge(result, start)
    }

    override fun getHomeElementsFromDatabase(
        tabFilter: HomeFilterType?,
        selectorFilter: SelectorFilterType?,
    ): Flow<RequestResult<List<HomeElementModel>>> {
        val request = homeDatabase
            .homeElementDao()
            .getAll()
            .toModel(tabFilter, selectorFilter)
            .catch {
                RequestResult.Error<RequestResult<List<HomeElementModel>>>(
                    error = it,
                )
            }

        val start = flowOf<RequestResult<List<HomeElementModel>>>(RequestResult.InProgress())

        return merge(start, request)
    }

    override fun getContent(
        id: String,
    ): Flow<RequestResult<ContentModel>> {
        val result = flowWithResult {
            serverRepository.loadContent(id)
        }.map(Result<ContentModel>::toRequestResult)

        val start = flowOf<RequestResult<ContentModel>>(RequestResult.InProgress())

        return merge(result, start)
    }

    private suspend fun saveHomeElementsToDatabase(
        data: List<HomeElementModel>,
    ) = homeDatabase.homeElementDao().insertAll(data.map(HomeElementModel::toEntity))

    private fun Flow<List<HomeElementEntity>>.toModel(
        tabFilter: HomeFilterType?,
        selectorFilter: SelectorFilterType?,
    ) = map {
        val databaseList = it.map(HomeElementEntity::toModel)
        when {
            tabFilter != null -> RequestResult.Success(databaseList.filterByHomeTab(tabFilter))
            selectorFilter != null -> RequestResult.Success(databaseList.filterBySelector(selectorFilter))
            else -> RequestResult.Success(databaseList)
        }
    }

    private fun List<HomeElementModel>.filterByHomeTab(
        filter: HomeFilterType,
    ) = when (filter) {
        HomeFilterType.YEAR -> groupBy {
            it.year
        }.values.map {
            it[0]
        }
        HomeFilterType.PLACE -> groupBy {
            it.place
        }.values.map {
            it[0]
        }
        HomeFilterType.COUNTRY -> groupBy {
            it.country
        }.values.map {
            it[0]
        }
        HomeFilterType.COMMON -> this
    }

    private fun List<HomeElementModel>.filterBySelector(
        filter: SelectorFilterType,
    ) = when (filter) {
        SelectorFilterType.YEAR -> filter {
            it.year == filter.value.toLong()
        }
        SelectorFilterType.COUNTRY -> filter {
            it.country == filter.value
        }
        SelectorFilterType.PLACE -> filter {
            it.place == filter.value
        }
    }
}