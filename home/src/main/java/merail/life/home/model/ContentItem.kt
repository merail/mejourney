package merail.life.home.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import merail.life.core.extensions.splitWithDelimiter
import merail.life.data.api.model.ContentModel

internal data class ContentItem(
    val title: String,
    val text: String,
    val imagesUrls: ImmutableList<String>,
)

internal fun ContentModel.toContentItem() = ContentItem(
    title = title,
    text = text,
    imagesUrls = imagesUrls.toImmutableList(),
)

internal const val IMAGE_DELIMITER = "<image>"

internal fun ContentItem.splitWithImages() = text.splitWithDelimiter(IMAGE_DELIMITER)