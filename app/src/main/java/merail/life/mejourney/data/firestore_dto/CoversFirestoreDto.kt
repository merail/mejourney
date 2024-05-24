package merail.life.mejourney.data.firestore_dto

import com.google.firebase.firestore.QuerySnapshot
import merail.life.mejourney.data.dto.CoverDto

@JvmInline
value class CoversFirestoreDto(
    val coverSnapshot: QuerySnapshot,
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