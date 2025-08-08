package merail.life.home.model

import kotlinx.collections.immutable.ImmutableList
import merail.life.core.extensions.splitWithDelimiter
import merail.life.data.api.model.ContentModel

internal class ContentItem(
    val title: String,
    val text: String,
    val imagesUrls: ImmutableList<String>,
)

internal fun ContentModel.toContentItem() = ContentItem(
    title = title,
    text = text,
    imagesUrls = imagesUrls,
)

internal const val IMAGE_DELIMITER = "<image>"

internal fun ContentItem.splitWithImages() = text.splitWithDelimiter(IMAGE_DELIMITER)