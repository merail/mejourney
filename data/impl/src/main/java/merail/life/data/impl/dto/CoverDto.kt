package merail.life.data.impl.dto

import merail.life.data.impl.server.dto.FirestoreDto

internal class CoverDto(
    val id: String,
    val year: Long,
    val country: String,
    val place: String,
    val title: String,
    val description: String,
)

internal fun FirestoreDto.toCoverDto() = snapshot.map {
    runCatching {
        CoverDto(
            id = it.id,
            year = it.data["year"] as Long,
            country = it.data["country"] as String,
            place = it.data["place"] as String,
            title = it.data["title"] as String,
            description = it.data["description"] as String,
        )
    }.onFailure {
        it.printStackTrace()
    }
}.filter(Result<CoverDto>::isSuccess).map(Result<CoverDto>::getOrThrow)