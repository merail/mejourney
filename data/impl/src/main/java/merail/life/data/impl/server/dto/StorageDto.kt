package merail.life.data.impl.server.dto

import android.net.Uri

@JvmInline
internal value class StorageDto(
    val reference: Uri,
)

internal fun List<Uri>.toDto() = map(::StorageDto)