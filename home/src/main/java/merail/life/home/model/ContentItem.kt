package merail.life.home.model

import kotlinx.collections.immutable.ImmutableList
import merail.life.core.extensions.splitWithDelimiter
import merail.life.data.data.model.ContentModel

class ContentItem(
    val title: String,
    val text: String,
    val imagesUrls: ImmutableList<String>,
)

fun ContentModel.toItem() = ContentItem(
    title = title,
    text = text,
    imagesUrls = imagesUrls,
)

const val IMAGE_DELIMITER = "<image>"

fun ContentItem.splitWithImages() = text.splitWithDelimiter(IMAGE_DELIMITER)