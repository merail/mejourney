package merail.life.core.extensions


fun <T, R> Iterable<T>.mapWithResult(
    transform: T.() -> R,
) = map {
    runCatching {
        transform(it)
    }.onFailure {
        it.printStackTrace()
    }
}.filter {
    it.isSuccess
}.map {
    it.getOrThrow()
}

inline val <T> Iterable<T>.isSingle
    get() = count() == 1