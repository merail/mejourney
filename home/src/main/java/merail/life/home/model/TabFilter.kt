package merail.life.home.model

import merail.life.firebase.data.model.HomeFilterType

enum class TabFilter {
    YEAR,
    COUNTRY,
    PLACE,
    COMMON,
    ;
}

fun TabFilter.toModel() = when (this) {
    TabFilter.YEAR -> HomeFilterType.YEAR
    TabFilter.COUNTRY -> HomeFilterType.COUNTRY
    TabFilter.PLACE -> HomeFilterType.PLACE
    TabFilter.COMMON -> HomeFilterType.COMMON
}