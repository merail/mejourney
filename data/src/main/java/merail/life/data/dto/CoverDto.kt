package merail.life.data.dto

import merail.life.api.data.model.FirestoreDto

internal class CoverDto(
    val id: String,
    val year: Long,
    val country: String,
    val place: String,
    val title: String,
    val description: String,
)

internal fun FirestoreDto.toCoverDto() = snapshot
    .map {
        with(it) {
            CoverDto(
                id = id,
                year = data["year"] as Long,
                country = data["country"] as String,
                place = data["place"] as String,
                title = data["title"] as String,
                description = data["description"] as String,
            )
        }
    }