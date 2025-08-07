package merail.life.core.extensions

inline val <T> Iterable<T>.isSingle
    get() = count() == 1