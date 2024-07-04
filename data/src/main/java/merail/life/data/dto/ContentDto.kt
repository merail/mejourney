package merail.life.data.data.dto

import merail.life.api.data.model.FirestoreDto

class ContentDto(
    val title: String,
    val text: String,
)

fun FirestoreDto.toContentDto() = snapshot
    .toList()
    .first()
    .run {
        ContentDto(
            title = data["title"] as String,
            text = data["text"] as String,
        )
    }