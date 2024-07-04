package merail.life.data.dto

import merail.life.api.data.model.FirestoreDto

internal class ContentDto(
    val title: String,
    val text: String,
)

internal fun FirestoreDto.toContentDto() = snapshot
    .toList()
    .first()
    .run {
        ContentDto(
            title = data["title"] as String,
            text = data["text"] as String,
        )
    }