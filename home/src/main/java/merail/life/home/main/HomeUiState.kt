package merail.life.home.main

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import merail.life.core.RequestResult
import merail.life.data.model.HomeElementModel
import merail.life.home.model.HomeItem
import merail.life.home.model.toHomeItems

sealed class HomeUiState(
    open val items: ImmutableList<HomeItem>,
) {
    data object None : HomeUiState(persistentListOf())

    data class Loading(
        override val items: ImmutableList<HomeItem>,
    ) : HomeUiState(items)

    data class Error(
        val exception: Throwable?,
        override val items: ImmutableList<HomeItem>,
    ) : HomeUiState(items)

    data class Success(
        override val items: ImmutableList<HomeItem>,
    ) : HomeUiState(items)
}

internal fun RequestResult<List<HomeElementModel>>.toState() = when (this) {
    is RequestResult.Error -> HomeUiState.Error(
        exception = error,
        items = data?.toHomeItems().orEmpty().toImmutableList(),
    )
    is RequestResult.InProgress -> HomeUiState.Loading(
        items = data?.toHomeItems().orEmpty().toImmutableList(),
    )
    is RequestResult.Success -> HomeUiState.Success(
        items = data.toHomeItems().toImmutableList(),
    )
}