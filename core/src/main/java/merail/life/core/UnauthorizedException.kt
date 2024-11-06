package merail.life.core

private const val PERMISSION_DENIED_MESSAGE = "PERMISSION_DENIED"

class UnauthorizedException: Exception()

fun Throwable.tryMapToUnauthorizedException() = if (message?.contains(PERMISSION_DENIED_MESSAGE) == true) {
    UnauthorizedException()
} else {
    null
}