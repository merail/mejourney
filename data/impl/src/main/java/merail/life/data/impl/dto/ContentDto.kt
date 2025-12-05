package merail.life.data.impl.dto

import merail.life.data.impl.server.dto.FirestoreDto

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