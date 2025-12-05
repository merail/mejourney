package merail.life.home.main

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import merail.life.core.mappers.RequestResult
import merail.life.data.api.model.HomeElementModel
import merail.life.home.model.HomeItem
import merail.life.home.model.toHomeItems

internal sealed class HomeLoadingState(
    open val items: ImmutableList<HomeItem>,
) {
    data class Loading(
        override val items: ImmutableList<HomeItem> = persistentListOf(),
    ) : HomeLoadingState(items)

    data class Error(
        val exception: Throwable?,
        override val items: ImmutableList<HomeItem>,
    ) : HomeLoadingState(items)

    data class Success(
        override val items: ImmutableList<HomeItem>,
    ) : HomeLoadingState(items)
}

internal fun RequestResult<List<HomeElementModel>>.toState() = when (this) {
    is RequestResult.InProgress -> HomeLoadingState.Loading(
        items = data?.toHomeItems().orEmpty().toImmutableList(),
    )
    is RequestResult.Error -> HomeLoadingState.Error(
        exception = error,
        items = data?.toHomeItems().orEmpty().toImmutableList(),
    )
    is RequestResult.Success -> HomeLoadingState.Success(
        items = data.toHomeItems().toImmutableList(),
    )
}