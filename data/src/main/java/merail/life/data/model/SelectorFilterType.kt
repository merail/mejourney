package merail.life.data.model

import androidx.annotation.Keep

@Keep
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