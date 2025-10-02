package merail.life.home.selector.state

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import merail.life.core.mappers.RequestResult
import merail.life.data.api.model.HomeElementModel
import merail.life.home.model.HomeItem
import merail.life.home.model.toHomeItems

internal sealed class SelectionLoadingState(
    open val items: ImmutableList<HomeItem>,
) {
    data object Loading: SelectionLoadingState(persistentListOf())

    data class Error(
        override val items: ImmutableList<HomeItem>,
        val exception: Throwable?,
    ): SelectionLoadingState(items)

    data class Success(
        override val items: ImmutableList<HomeItem>,
    ): SelectionLoadingState(items)
}

internal fun RequestResult<List<HomeElementModel>>.toState() = when (this) {
    is RequestResult.Error -> SelectionLoadingState.Error(
        exception = error,
        items = data?.toHomeItems().orEmpty().toImmutableList(),
    )
    is RequestResult.InProgress -> SelectionLoadingState.Loading
    is RequestResult.Success -> SelectionLoadingState.Success(
        items = data.toHomeItems().toImmutableList(),
    )
}