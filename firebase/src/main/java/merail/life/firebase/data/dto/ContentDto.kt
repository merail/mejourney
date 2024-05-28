package merail.life.firebase.data.dto

import com.google.firebase.firestore.QuerySnapshot

@JvmInline
value class ContentFirestoreDto(
    val contentSnapshot: QuerySnapshot,
)

class ContentDto(
    val title: String,
    val text: String,
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