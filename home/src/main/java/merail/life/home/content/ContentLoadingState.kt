package merail.life.home.content

import merail.life.core.RequestResult
import merail.life.data.model.ContentModel
import merail.life.home.model.ContentItem
import merail.life.home.model.toContentItem

internal sealed class ContentLoadingState {

    data object Loading: ContentLoadingState()

    data class Error(val exception: Throwable?): ContentLoadingState()

    class Success(
        val item: ContentItem,
    ): ContentLoadingState()
}

internal fun RequestResult<ContentModel>.toState() = when (this) {
    is RequestResult.Error -> ContentLoadingState.Error(
        exception = error,
    )
    is RequestResult.InProgress -> ContentLoadingState.Loading
    is RequestResult.Success -> ContentLoadingState.Success(
        item = data.toContentItem(),
    )
}