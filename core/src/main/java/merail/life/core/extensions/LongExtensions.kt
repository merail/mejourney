package merail.life.core.extensions

import java.util.Locale

fun Long.toCountdownTime(): String {
    val remainingMinutes = this / 60
    val remainingSeconds = this % 60
    return String.format(
        locale = Locale("ru"),
        format = "%02d:%02d",
        remainingMinutes,
        remainingSeconds,
    )
}