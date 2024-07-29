package merail.life.core.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

fun <T> flowWithResult(block: suspend () -> T): Flow<Result<T>> = flow {
    emit(
        value = runCatching {
            block()
        },
    )
}

fun <T> Flow<T>.catchWithResult(action: suspend (cause: Throwable) -> T): Flow<T> = catch {
    emit(
        value = action(it),
    )
}
