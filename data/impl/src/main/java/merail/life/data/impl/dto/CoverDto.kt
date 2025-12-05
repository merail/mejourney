package merail.life.data.impl.dto

import merail.life.core.constants.HomeElementsFields
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
            year = it.data[HomeElementsFields.YEAR] as Long,
            country = it.data[HomeElementsFields.COUNTRY] as String,
            place = it.data[HomeElementsFields.PLACE] as String,
            title = it.data[HomeElementsFields.TITLE] as String,
            description = it.data[HomeElementsFields.DESCRIPTION] as String,
        )
    }.onFailure {
        it.printStackTrace()
    }
}.filter(Result<CoverDto>::isSuccess).map(Result<CoverDto>::getOrThrow)