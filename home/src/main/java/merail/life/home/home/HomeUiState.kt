package merail.life.home.home

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import merail.life.firebase.data.RequestResult
import merail.life.firebase.data.model.HomeElementModel
import merail.life.home.model.HomeItem
import merail.life.home.model.toItems

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

fun RequestResult<List<HomeElementModel>>.toState() = when (this) {
    is RequestResult.Error -> HomeUiState.Error(
        exception = error,
        items = data?.toItems().orEmpty().toImmutableList(),
    )

    is RequestResult.InProgress -> HomeUiState.Loading(
        items = data?.toItems().orEmpty().toImmutableList(),
    )

    is RequestResult.Success -> HomeUiState.Success(
        items = data.toItems().toImmutableList(),
    )
}