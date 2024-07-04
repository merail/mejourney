package merail.life.data

import merail.life.core.RequestResult
import merail.life.core.RequestResult.Error
import merail.life.core.RequestResult.InProgress
import merail.life.core.RequestResult.Success

internal interface MergeStrategy<E> {
    fun merge(
        right: E,
        left: E,
    ): E
}

internal class RequestResponseMergeStrategy<T : Any> : MergeStrategy<RequestResult<T>> {
    @Suppress("CyclomaticComplexMethod")
    override fun merge(
        right: RequestResult<T>,
        left: RequestResult<T>
    ): RequestResult<T> {
        return when {
            right is InProgress && left is InProgress -> merge(right, left)
            right is Success && left is InProgress -> merge(right, left)
            right is InProgress && left is Success -> merge(right, left)
            right is Success && left is Success -> merge(right, left)
            right is Success && left is Error -> merge(right, left)
            right is InProgress && left is Error -> merge(right, left)
            right is Error && left is InProgress -> merge(right, left)
            right is Error && left is Success -> merge(right, left)
            else -> error("Unimplemented branch right=$right & left=$left")
        }
    }

    private fun merge(
        cache: InProgress<T>,
        server: InProgress<T>,
    ) = when {
        server.data != null -> InProgress(server.data)
        else -> InProgress(cache.data)
    }

    private fun merge(
        cache: Success<T>,
        server: InProgress<T>,
    ) = InProgress(cache.data)

    private fun merge(
        cache: InProgress<T>,
        server: Success<T>,
    ) = InProgress(server.data)

    private fun merge(
        cache: Success<T>,
        server: Error<T>,
    ) = Error(
        data = cache.data,
        error = server.error,
    )

    private fun merge(
        cache: Success<T>,
        server: Success<T>,
    ) = Success(server.data)

    private fun merge(
        cache: InProgress<T>,
        server: Error<T>,
    ) = Error(
        data = server.data ?: cache.data,
        error = server.error,
    )

    private fun merge(
        cache: Error<T>,
        server: InProgress<T>,
    ) = server

    private fun merge(
        cache: Error<T>,
        server: Success<T>,
    ) = server
}