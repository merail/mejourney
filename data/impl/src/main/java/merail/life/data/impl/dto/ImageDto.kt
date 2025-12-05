package merail.life.data.impl.dto

import merail.life.data.impl.server.dto.StorageDto

internal class ImageDto(
    val reference: String,
)

internal fun StorageDto.toImageDto() = ImageDto(reference.toString())