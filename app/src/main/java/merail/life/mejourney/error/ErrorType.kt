package merail.life.mejourney.error

import merail.life.core.NoInternetConnectionException

internal enum class ErrorType {
    INTERNET_CONNECTION,
    OTHER,
    ;
}

internal fun Throwable?.toType() = when (this) {
    is NoInternetConnectionException -> ErrorType.INTERNET_CONNECTION
    else -> ErrorType.OTHER
}