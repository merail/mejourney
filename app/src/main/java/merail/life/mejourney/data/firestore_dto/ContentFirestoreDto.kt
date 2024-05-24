package merail.life.mejourney.data.firestore_dto

import com.google.firebase.firestore.QuerySnapshot
import merail.life.mejourney.data.dto.ContentDto

@JvmInline
value class ContentFirestoreDto(
    val contentSnapshot: QuerySnapshot,
)

fun ContentFirestoreDto.toDto() = contentSnapshot
    .toList()
    .first()
    .run {
        ContentDto(
            title = data["title"] as String,
            text = data["text"] as String,
        )
    }