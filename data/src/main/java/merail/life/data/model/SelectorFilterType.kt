package merail.life.data.model

enum class SelectorFilterType {
    YEAR {
        override var value = ""
    },
    COUNTRY {
        override var value = ""
    },
    PLACE {
        override var value = ""
    },
    ;

    abstract var value: String
}