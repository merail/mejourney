package merail.life.api.data.model

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import merail.life.api.data.ServerRepository

@JvmInline
value class StorageDto(
    val reference: Uri,
)

suspend fun StorageReference.toDto() = downloadUrl
    .addOnFailureListener {
        Log.w(ServerRepository.TAG, "Getting $name url from Firebase Storage. Failure")
    }
    .addOnSuccessListener {
        Log.d(ServerRepository.TAG, "Getting $name url from Firebase Storage. Success: $it")
    }
    .await()
    .run {
        StorageDto(this)
    }