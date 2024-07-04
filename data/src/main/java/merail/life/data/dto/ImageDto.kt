package merail.life.data.dto

import merail.life.api.data.model.StorageDto

internal class ImageDto(
    val reference: String,
)

internal fun StorageDto.toImageDto() = ImageDto(reference.toString())