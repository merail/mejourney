package merail.life.home.model

import merail.life.data.data.model.HomeElementModel

class HomeItem(
    val id: String,
    val year: Long,
    val country: String,
    val place: String,
    val title: String,
    val description: String,
    val url: String,
)

fun HomeElementModel.toItem() = HomeItem(
    id = id,
    year = year,
    country = country,
    place = place,
    title = title,
    description = description,
    url = url,
)

fun List<HomeElementModel>.toItems() = map(HomeElementModel::toItem)