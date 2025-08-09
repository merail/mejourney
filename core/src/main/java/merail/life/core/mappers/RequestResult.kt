package merail.life.core.mappers

sealed class RequestResult<out E : Any>(
    open val data: E?,
) {
    data class InProgress<E : Any>(
        override val data: E? = null,
    ) : RequestResult<E>(data)

    data class Success<E : Any>(
        override val data: E,
    ) : RequestResult<E>(data)

    data class Error<E : Any>(
        override val data: E? = null,
        val error: Throwable? = null,
    ) : RequestResult<E>(data)
}

fun <I : Any, O : Any> RequestResult<I>.map(mapper: (I) -> O) = when (this) {
    is RequestResult.Success -> RequestResult.Success(mapper(data))
    is RequestResult.Error -> RequestResult.Error(data?.let(mapper))
    is RequestResult.InProgress -> RequestResult.InProgress(data?.let(mapper))
}

fun <T : Any> Result<T>.toRequestResult() = when {
    isSuccess -> RequestResult.Success(getOrThrow())
    isFailure -> RequestResult.Error(
        error = exceptionOrNull(),
    )
    else -> error("Impossible branch")
}