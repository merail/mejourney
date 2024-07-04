package merail.life.home.model

import merail.life.data.data.model.SelectorFilterModel

sealed class SelectorFilter {

    class Year(val year: Long): SelectorFilter()

    class Country(val country: String): SelectorFilter()

    class Place(val place: String): SelectorFilter()
}

fun SelectorFilter.toModel() = when (this) {
    is SelectorFilter.Year -> SelectorFilterModel.Year(year)
    is SelectorFilter.Country -> SelectorFilterModel.Country(country)
    is SelectorFilter.Place -> SelectorFilterModel.Place(place)
}