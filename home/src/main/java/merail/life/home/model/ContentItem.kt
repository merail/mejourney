package merail.life.home.model

import kotlinx.collections.immutable.ImmutableList
import merail.life.firebase.data.model.ContentModel

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

fun ContentItem.splitText() = text.split("[image]")