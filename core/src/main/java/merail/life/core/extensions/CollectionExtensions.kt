package merail.life.core.extensions


import android.util.Log

fun <T, R> Iterable<T>.mapWithResult(
    tag: String = "mapWithResult",
    transform: T.() -> R,
) = map {
    runCatching {
        transform(it)
    }.onFailure {
        Log.w(tag, it)
    }
}.filter {
    it.isSuccess
}.map {
    it.getOrThrow()
}

inline val <T> Iterable<T>.isSingle
    get() = count() == 1