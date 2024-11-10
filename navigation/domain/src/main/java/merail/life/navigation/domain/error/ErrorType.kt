package merail.life.navigation.domain.error

import androidx.annotation.Keep
import merail.life.core.NoInternetConnectionException

@Keep
enum class ErrorType {
    INTERNET_CONNECTION,
    OTHER,
    ;
}

fun Throwable?.toType() = when (this) {
    is NoInternetConnectionException -> ErrorType.INTERNET_CONNECTION
    else -> ErrorType.OTHER
}