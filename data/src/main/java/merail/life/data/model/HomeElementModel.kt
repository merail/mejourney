package merail.life.data.data.model

import merail.life.api.database.model.HomeElementEntity

data class HomeElementModel(
    val id: String,
    val year: Long,
    val country: String,
    val place: String,
    val title: String,
    val description: String,
    val url: String,
)

fun HomeElementEntity.toModel() = HomeElementModel(
    id = id,
    year = year,
    country = country,
    place = place,
    title = title,
    description = description,
    url = url,
)

fun HomeElementModel.toEntity() = HomeElementEntity(
    id = id,
    year = year,
    country = country,
    place = place,
    title = title,
    description = description,
    url = url,
)