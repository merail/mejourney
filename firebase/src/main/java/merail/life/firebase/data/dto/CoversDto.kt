package merail.life.firebase.data.dto

import com.google.firebase.firestore.QuerySnapshot

@JvmInline
value class CoversFirestoreDto(
    val coverSnapshot: QuerySnapshot,
)

class CoverDto(
    val id: String,
    val year: Long,
    val country: String,
    val place: String,
    val title: String,
    val description: String,
)

fun CoversFirestoreDto.toDto() = coverSnapshot
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