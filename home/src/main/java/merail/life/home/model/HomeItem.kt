package merail.life.home.model

import merail.life.firebase.data.model.HomeModel

class HomeItem(
    val id: String,
    val year: Long,
    val country: String,
    val place: String,
    val title: String,
    val description: String,
    val url: String,
)

fun HomeModel.toItem() = HomeItem(
    id = id,
    year = year,
    country = country,
    place = place,
    title = title,
    description = description,
    url = url,
)

fun List<HomeModel>.toItems() = map(HomeModel::toItem)