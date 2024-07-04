package merail.life.data.model

sealed class SelectorFilterModel {

    class Year(val year: Long): SelectorFilterModel()

    class Country(val country: String): SelectorFilterModel()

    class Place(val place: String): SelectorFilterModel()
}