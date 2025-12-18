package merail.life.data.impl.server.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class ContentDto(
    val id: String,
    val coverId: String,
    val title: String,
    val body: String,
    val imagesUrls: List<String>,
)