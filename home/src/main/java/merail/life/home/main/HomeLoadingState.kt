package merail.life.home.main

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import merail.life.core.RequestResult
import merail.life.core.errors.UnauthorizedException
import merail.life.data.model.HomeElementModel
import merail.life.home.model.HomeItem
import merail.life.home.model.toHomeItems

internal sealed class HomeLoadingState(
    open val items: ImmutableList<HomeItem>,
) {
    data object None : HomeLoadingState(persistentListOf())

    data class Loading(
        override val items: ImmutableList<HomeItem>,
    ) : HomeLoadingState(items)

    data class UnauthorizedException(
        val exception: Throwable?,
        override val items: ImmutableList<HomeItem>,
    ) : HomeLoadingState(items)

    data class CommonError(
        val exception: Throwable?,
        override val items: ImmutableList<HomeItem>,
    ) : HomeLoadingState(items)

    data class Success(
        override val items: ImmutableList<HomeItem>,
    ) : HomeLoadingState(items)
}

internal fun RequestResult<List<HomeElementModel>>.toState() = when (this) {
    is RequestResult.Error -> when {
        error is UnauthorizedException -> HomeLoadingState.UnauthorizedException(
            exception = error,
            items = data?.toHomeItems().orEmpty().toImmutableList(),
        )
        else -> HomeLoadingState.CommonError(
            exception = error,
            items = data?.toHomeItems().orEmpty().toImmutableList(),
        )
    }
    is RequestResult.InProgress -> HomeLoadingState.Loading(
        items = data?.toHomeItems().orEmpty().toImmutableList(),
    )
    is RequestResult.Success -> HomeLoadingState.Success(
        items = data.toHomeItems().toImmutableList(),
    )
}