package merail.life.design.components

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import merail.life.design.extensions.createMediaRequest

@Composable
fun ContentImage(
    url: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillWidth,
) {
    SubcomposeAsyncImage(
        model = LocalContext.current.createMediaRequest(url),
        contentDescription = null,
        loading = {
            ImageLoading(Modifier.height(512.dp))
        },
        contentScale = contentScale,
        modifier = modifier,
    )
}