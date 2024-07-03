package merail.life.api.data.model

import android.util.Log
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import merail.life.api.data.DataRepository

@JvmInline
value class StorageDto(
    val reference: StorageReference,
)

fun StorageReference.toDto() = StorageDto(this)

suspend fun StorageDto.getUrl() = reference
    .downloadUrl
    .addOnFailureListener {
        Log.w(DataRepository.TAG, "Getting ${reference.name} url from Firebase Storage. Failure")
    }
    .addOnSuccessListener {
        Log.d(DataRepository.TAG, "Getting ${reference.name} url from Firebase Storage. Success: $it")
    }
    .await()
    .toString()