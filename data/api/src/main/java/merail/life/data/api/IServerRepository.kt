package merail.life.data.api

import merail.life.data.api.model.ContentModel
import merail.life.data.api.model.HomeElementModel

interface IServerRepository {

    suspend fun loadCovers(): List<HomeElementModel>

    suspend fun loadContent(coverId: String): ContentModel
}