package merail.life.firebase.data

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
import merail.life.api.data.IDataRepository
import merail.life.api.data.model.getUrl
import merail.life.api.database.HomeDatabase
import merail.life.api.database.model.HomeElementEntity
import merail.life.firebase.BuildConfig
import merail.life.firebase.data.dto.toContentDto
import merail.life.firebase.data.dto.toCoverDto
import merail.life.firebase.data.model.ContentModel
import merail.life.firebase.data.model.HomeElementModel
import merail.life.firebase.data.model.HomeFilterType
import merail.life.firebase.data.model.SelectorFilterModel
import merail.life.firebase.data.model.toEntity
import merail.life.firebase.data.model.toModel
import javax.inject.Inject

class FirebaseRepository @Inject constructor(
    private val homeDatabase: HomeDatabase,
    private val dataRepository: IDataRepository,
) : IFirebaseRepository {

    companion object {
        private const val TAG = "FirebaseRepository"

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
            val result = runCatching {
                val firestoreData = dataRepository.getFirestoreData(HOME_COVERS_PATH)
                val storageData = dataRepository.getStorageData(HOME_COVERS_PATH)
                firestoreData.toCoverDto().zip(storageData).map { (info, file) ->
                    HomeElementModel(
                        id = info.id,
                        year = info.year,
                        country = info.country,
                        place = info.place,
                        title = info.title,
                        description = info.description,
                        url = file.getUrl(),
                    )
                }
            }
            emit(result)
        }.onEach { result ->
            if (result.isSuccess) {
                Log.d(TAG, "Getting home elements from server. Success")
                saveHomeElementsToDatabase(result.getOrThrow())
            }
        }.onEach { result ->
            if (result.isFailure) {
                Log.w(TAG, "Getting home elements from server. Failure", result.exceptionOrNull())
            }
        }.map {
            it.toRequestResult()
        }
        val start = flowOf<RequestResult<List<HomeElementModel>>>(RequestResult.InProgress())
        return merge(result, start)
    }

    override fun getHomeElementsFromDatabase(
        tabFilter: HomeFilterType?,
        selectorFilter: SelectorFilterModel?
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
                emit(
                    value = RequestResult.Error(
                        error = it,
                    ),
                )
            }
        val start = flowOf<RequestResult<List<HomeElementModel>>>(RequestResult.InProgress())
        return merge(start, request)
    }

    private suspend fun saveHomeElementsToDatabase(
        data: List<HomeElementModel>,
    ) = homeDatabase.homeElementDao().insertAll(data.map(HomeElementModel::toEntity))

    override suspend fun getContent(
        id: String,
    ): ContentModel {
        val firestoreData = dataRepository.getFirestoreData("$CONTENT_PATH$id")
        val storageData = dataRepository.getStorageData("$CONTENT_PATH$id")
        val contentDto = firestoreData.toContentDto()
        val imagesUrls = storageData.map { file ->
            file.getUrl()
        }
        return ContentModel(
            title = contentDto.title,
            text = contentDto.text,
            imagesUrls = imagesUrls.toImmutableList(),
        )
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
        filter: SelectorFilterModel,
    ) = when (filter) {
        is SelectorFilterModel.Year -> filter {
            it.year == filter.year
        }
        is SelectorFilterModel.Country -> filter {
            it.country == filter.country
        }
        is SelectorFilterModel.Place -> filter {
            it.place == filter.place
        }
    }
}