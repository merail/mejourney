package merail.life.navigation.domain.error

import merail.life.core.NoInternetConnectionException

enum class ErrorType {
    INTERNET_CONNECTION,
    OTHER,
    ;
}

fun Throwable?.toType() = when (this) {
    is NoInternetConnectionException -> ErrorType.INTERNET_CONNECTION
    else -> ErrorType.OTHER
}