package merail.life.data.impl.server

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import merail.life.data.api.IServerRepository
import merail.life.data.api.model.ContentModel
import merail.life.data.api.model.HomeElementModel
import merail.life.data.impl.BuildConfig
import javax.inject.Inject

internal class ServerRepository @Inject constructor(
    private val serverApi: ServerApi,
) : IServerRepository {

    override suspend fun loadCovers() = withContext(Dispatchers.IO) {
        serverApi.getCovers().map {
            HomeElementModel(
                id = it.id,
                year = it.year,
                country = it.country,
                place = it.place,
                title = it.title,
                description = it.description,
                imageUrl = BuildConfig.DOMAIN_URL + it.imageUrl,
            )
        }
    }

    override suspend fun loadContent(
        coverId: String,
    ) = withContext(Dispatchers.IO) {
        serverApi.getContent(coverId).let {
            ContentModel(
                id = it.id,
                title = it.title,
                text = it.body,
                imagesUrls = it.imagesUrls.map { imageUrl ->
                    BuildConfig.DOMAIN_URL + imageUrl
                },
            )
        }
    }
}