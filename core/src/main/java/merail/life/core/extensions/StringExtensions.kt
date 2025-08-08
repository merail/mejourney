package merail.life.core.extensions

val String.Companion.Slash
    get() = "/"

fun String.splitWithDelimiter(
    delimiter: String,
) = Regex("((?<=%1\$s)|(?=%1\$s))".format(delimiter)).split(this).filter { it.isNotBlank() }