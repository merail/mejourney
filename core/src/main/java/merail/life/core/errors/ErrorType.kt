package merail.life.core.errors

import androidx.annotation.Keep

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