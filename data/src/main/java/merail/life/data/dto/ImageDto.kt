package merail.life.data.data.dto

import merail.life.api.data.model.StorageDto

class ImageDto(
    val reference: String,
)

fun StorageDto.toImageDto() = ImageDto(reference.toString())