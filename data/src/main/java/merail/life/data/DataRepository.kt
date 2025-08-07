package merail.life.data

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import merail.life.api.data.IServerRepository
import merail.life.api.data.model.StorageDto
import merail.life.api.database.HomeDatabase
import merail.life.api.database.model.HomeElementEntity
import merail.life.core.MergeStrategy
import merail.life.core.RequestResponseMergeStrategy
import merail.life.core.RequestResult
import merail.life.core.extensions.flowWithResult
import merail.life.core.toRequestResult
import merail.life.data.dto.ImageDto
import merail.life.data.dto.toContentDto
import merail.life.data.dto.toCoverDto
import merail.life.data.dto.toImageDto
import merail.life.data.model.ContentModel
import merail.life.data.model.HomeElementModel
import merail.life.data.model.HomeFilterType
import merail.life.data.model.SelectorFilterType
import merail.life.data.model.toEntity
import merail.life.data.model.toModel
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val homeDatabase: HomeDatabase,
    private val serverRepository: IServerRepository,
) : IDataRepository {

    companion object {

        private const val HOME_COVERS_PATH = "home_covers"

        private const val CONTENT_PATH = ""
    }

    override fun getHomeElements(): Flow<RequestResult<List<HomeElementModel>>> {
        val mergeStrategy: MergeStrategy<RequestResult<List<HomeElementModel>>> = RequestResponseMergeStrategy()

        val cachedHomeElements = getHomeElementsFromDatabase()
        val remoteHomeElements = getHomeElementsFromServer()

        return cachedHomeElements.combine(remoteHomeElements, mergeStrategy::merge)
    }

    private fun getHomeElementsFromServer(): Flow<RequestResult<List<HomeElementModel>>> {
        val result = flowWithResult {
            val firestoreData = serverRepository.getFirestoreData(
                folderName = HOME_COVERS_PATH,
            ).toCoverDto()
            val storageData = serverRepository.getStorageData(
                folderName = HOME_COVERS_PATH,
            ).map(StorageDto::toImageDto)

            firestoreData.zip(storageData).map { (info, file) ->
                HomeElementModel(
                    id = info.id,
                    year = info.year,
                    country = info.country,
                    place = info.place,
                    title = info.title,
                    description = info.description,
                    url = file.reference,
                )
            }
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
            .map {
                val databaseList = it.map(HomeElementEntity::toModel)
                when {
                    tabFilter != null -> RequestResult.Success(databaseList.filterByHomeTab(tabFilter))
                    selectorFilter != null -> RequestResult.Success(databaseList.filterBySelector(selectorFilter))
                    else -> RequestResult.Success(databaseList)
                }
            }.catch {
                RequestResult.Error<RequestResult<List<HomeElementModel>>>(error = it)
            }

        val start = flowOf<RequestResult<List<HomeElementModel>>>(RequestResult.InProgress())

        return merge(start, request)
    }

    override fun getContent(
        id: String,
    ): Flow<RequestResult<ContentModel>> {
        val result = flowWithResult {
            val firestoreData = serverRepository.getFirestoreData(
                folderName = "$CONTENT_PATH$id",
            ).toContentDto()
            val storageData = serverRepository.getStorageData(
                folderName = "$CONTENT_PATH$id",
            ).map(StorageDto::toImageDto)

            ContentModel(
                title = firestoreData.title,
                text = firestoreData.text,
                imagesUrls = storageData.map(ImageDto::reference).toImmutableList(),
            )
        }.map(Result<ContentModel>::toRequestResult)

        val start = flowOf<RequestResult<ContentModel>>(RequestResult.InProgress())

        return merge(result, start)
    }

    private suspend fun saveHomeElementsToDatabase(
        data: List<HomeElementModel>,
    ) = homeDatabase.homeElementDao().insertAll(data.map(HomeElementModel::toEntity))

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