package merail.life.data.impl.server.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class CoverDto(
    val id: String,
    val year: Long,
    val country: String,
    val place: String,
    val title: String,
    val description: String,
    val imageUrl: String,
)