package merail.life.firebase.data.model

import kotlinx.collections.immutable.ImmutableList

data class ContentItem(
    val title: String,
    val text: String,
    val imagesUrls: ImmutableList<String>,
)

fun ContentItem.splitText() = text.split("[image]")