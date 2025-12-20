package merail.life.data.api.model

data class ContentModel(
    val id: String,
    val title: String,
    val text: String,
    val imagesUrls: List<String>,
)