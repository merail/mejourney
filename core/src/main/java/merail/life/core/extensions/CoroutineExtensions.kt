package merail.life.core.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
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

suspend fun <T> retrySuspend(
    times: Int = Int.MAX_VALUE,
    block: suspend () -> T,
) = flow {
    emit(block())
}.retry(times.toLong()).first()
