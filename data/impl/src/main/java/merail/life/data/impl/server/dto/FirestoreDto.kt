package merail.life.data.impl.server.dto

import com.google.firebase.firestore.QuerySnapshot

internal data class FirestoreDto(
    val snapshot: QuerySnapshot,
)

internal fun QuerySnapshot.toDto() = FirestoreDto(this)