package merail.life.firebase.data.model

import kotlinx.collections.immutable.ImmutableList

data class ContentModel(
    val title: String,
    val text: String,
    val imagesUrls: ImmutableList<String>,
)