package merail.life.home.selector

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import merail.life.core.RequestResult
import merail.life.data.model.HomeElementModel
import merail.life.home.model.HomeItem
import merail.life.home.model.toHomeItems

sealed class SelectorUiState(
    open val items: ImmutableList<HomeItem>,
) {

    data object None: SelectorUiState(persistentListOf())

    data class Loading(
        override val items: ImmutableList<HomeItem>,
    ): SelectorUiState(items)

    data class Error(
        override val items: ImmutableList<HomeItem>,
        val exception: Throwable?,
    ): SelectorUiState(items)

    data class Success(
        override val items: ImmutableList<HomeItem>,
    ): SelectorUiState(items)
}

internal fun RequestResult<List<HomeElementModel>>.toState() = when (this) {
    is RequestResult.Error -> SelectorUiState.Error(
        exception = error,
        items = data?.toHomeItems().orEmpty().toImmutableList(),
    )
    is RequestResult.InProgress -> SelectorUiState.Loading(
        items = data?.toHomeItems().orEmpty().toImmutableList(),
    )
    is RequestResult.Success -> SelectorUiState.Success(
        items = data.toHomeItems().toImmutableList(),
    )
}