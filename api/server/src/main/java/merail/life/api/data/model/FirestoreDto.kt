package merail.life.api.data.model

import com.google.firebase.firestore.QuerySnapshot

@JvmInline
value class FirestoreDto(
    val snapshot: QuerySnapshot,
)

fun QuerySnapshot.toDto() = FirestoreDto(this)