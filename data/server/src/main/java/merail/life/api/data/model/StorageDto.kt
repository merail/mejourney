package merail.life.api.data.model

import android.net.Uri

@JvmInline
value class StorageDto(
    val reference: Uri,
)

fun List<Uri>.toDto() = map(::StorageDto)