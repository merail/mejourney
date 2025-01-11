package merail.life.home.model

import merail.life.data.model.HomeElementModel

internal class HomeItem(
    val id: String,
    val year: Long,
    val country: String,
    val place: String,
    val title: String,
    val description: String,
    val url: String,
)

internal fun HomeElementModel.toHomeItem() = HomeItem(
    id = id,
    year = year,
    country = country,
    place = place,
    title = title,
    description = description,
    url = url,
)

internal fun List<HomeElementModel>.toHomeItems() = map(HomeElementModel::toHomeItem)