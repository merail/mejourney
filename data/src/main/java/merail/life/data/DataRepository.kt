package merail.life.data

import android.util.Log
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import merail.life.api.data.IServerRepository
import merail.life.api.data.model.StorageDto
import merail.life.api.database.HomeDatabase
import merail.life.api.database.model.HomeElementEntity
import merail.life.core.RequestResult
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
        private const val TAG = "DataRepository"

        private const val MAIN_PATH = BuildConfig.FIREBASE_REPOSITORY_PATH

        private const val HOME_COVERS_PATH = "$MAIN_PATH/home_covers"

        private const val CONTENT_PATH = "$MAIN_PATH/"
    }

    override fun getHomeElements(): Flow<RequestResult<List<HomeElementModel>>> {
        val mergeStrategy: MergeStrategy<RequestResult<List<HomeElementModel>>> = RequestResponseMergeStrategy()
        val cachedAllArticles: Flow<RequestResult<List<HomeElementModel>>> = getHomeElementsFromDatabase()
        val remoteArticles: Flow<RequestResult<List<HomeElementModel>>> = getHomeElementsFromServer()
        return cachedAllArticles.combine(remoteArticles, mergeStrategy::merge)
    }

    private fun getHomeElementsFromServer(): Flow<RequestResult<List<HomeElementModel>>> {
        val result = flow {
            emit(
                value = runCatching {
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
                }
            )
        }.onEach {
            if (it.isSuccess) {
                saveHomeElementsToDatabase(it.getOrThrow())
            }
        }.map {
            Log.d(TAG, "Getting home elements from server. Success")
            it.toRequestResult()
        }.catch {
            Log.w(TAG, "Getting home elements from server. Failure", it)
            emit(RequestResult.Error(error = it))
        }
        val start = flowOf<RequestResult<List<HomeElementModel>>>(RequestResult.InProgress())
        return merge(result, start)
    }

    override fun getHomeElementsFromDatabase(
        tabFilter: HomeFilterType?,
        selectorFilter: SelectorFilterType?,
    ): Flow<RequestResult<List<HomeElementModel>>> {
        val request = homeDatabase
            .homeElementDao()::getAll
            .asFlow()
            .map<List<HomeElementEntity>, RequestResult<List<HomeElementModel>>> {
                Log.d(TAG, "Getting home elements from database. Success")
                val databaseList = it.map(HomeElementEntity::toModel)
                when {
                    tabFilter != null -> RequestResult.Success(databaseList.filterByHomeTab(tabFilter))
                    selectorFilter != null -> RequestResult.Success(databaseList.filterBySelector(selectorFilter))
                    else -> RequestResult.Success(databaseList)
                }
            }
            .catch {
                Log.w(TAG, "Getting home elements from database. Failure", it)
                emit(RequestResult.Error(error = it))
            }
        val start = flowOf<RequestResult<List<HomeElementModel>>>(RequestResult.InProgress())
        return merge(start, request)
    }

    private suspend fun saveHomeElementsToDatabase(
        data: List<HomeElementModel>,
    ) = homeDatabase.homeElementDao().insertAll(data.map(HomeElementModel::toEntity))

    override fun getContent(
        id: String,
    ): Flow<RequestResult<ContentModel>> {
        val result = flow {
            emit(
                value = runCatching {
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
                }
            )
        }.map {
            Log.d(TAG, "Getting content from server. Success")
            it.toRequestResult()
        }.catch {
            Log.w(TAG, "Getting content from server. Failure", it)
            emit(RequestResult.Error(error = it))
        }
        val start = flowOf<RequestResult<ContentModel>>(RequestResult.InProgress())
        return merge(result, start)
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