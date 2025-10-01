package merail.life.home.selector.state

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import merail.life.core.mappers.RequestResult
import merail.life.data.api.model.HomeElementModel
import merail.life.home.model.HomeItem
import merail.life.home.model.toHomeItems

internal sealed class SelectionState(
    open val items: ImmutableList<HomeItem>,
) {
    data object Loading: SelectionState(persistentListOf())

    data class Error(
        override val items: ImmutableList<HomeItem>,
        val exception: Throwable?,
    ): SelectionState(items)

    data class Success(
        override val items: ImmutableList<HomeItem>,
    ): SelectionState(items)
}

internal fun RequestResult<List<HomeElementModel>>.toState() = when (this) {
    is RequestResult.Error -> SelectionState.Error(
        exception = error,
        items = data?.toHomeItems().orEmpty().toImmutableList(),
    )
    is RequestResult.InProgress -> SelectionState.Loading
    is RequestResult.Success -> SelectionState.Success(
        items = data.toHomeItems().toImmutableList(),
    )
}