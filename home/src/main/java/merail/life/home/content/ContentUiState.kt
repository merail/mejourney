package merail.life.home.content

import merail.life.core.RequestResult
import merail.life.data.data.model.ContentModel
import merail.life.home.model.ContentItem
import merail.life.home.model.toItem

sealed class ContentUiState {

    data object Loading: ContentUiState()

    data class Error(val exception: Throwable?): ContentUiState()

    class Success(
        val item: ContentItem,
    ): ContentUiState()
}

fun RequestResult<ContentModel>.toState() = when (this) {
    is RequestResult.Error -> ContentUiState.Error(
        exception = error,
    )
    is RequestResult.InProgress -> ContentUiState.Loading
    is RequestResult.Success -> ContentUiState.Success(
        item = data.toItem(),
    )
}