package merail.life.home.model

import merail.life.data.model.SelectorFilterType

internal sealed class SelectorFilter {

    class Year(val year: Long): SelectorFilter()

    class Country(val country: String): SelectorFilter()

    class Place(val place: String): SelectorFilter()
}

internal fun SelectorFilter.toModel() = when (this) {
    is SelectorFilter.Year -> SelectorFilterType.YEAR.apply { value = year.toString() }
    is SelectorFilter.Country -> SelectorFilterType.COUNTRY.apply { value = country }
    is SelectorFilter.Place -> SelectorFilterType.PLACE.apply { value = place }
}