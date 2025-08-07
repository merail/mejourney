package merail.life.core.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.cancellation.CancellationException

inline fun <R> suspendableRunCatching(
    block: () -> R,
): Result<R> = try {
    Result.success(block())
} catch (c: CancellationException) {
    throw c
} catch (e: Throwable) {
    Result.failure(e)
}

fun <T> flowWithResult(block: suspend () -> T): Flow<Result<T>> = flow {
    emit(
        value = suspendableRunCatching {
            block()
        },
    )
}
