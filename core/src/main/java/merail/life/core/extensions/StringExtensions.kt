package merail.life.core.extensions

fun String.splitWithDelimiter(
    delimiter: String,
) = Regex("((?<=%1\$s)|(?=%1\$s))".format(delimiter)).split(this).filter { it.isNotBlank() }