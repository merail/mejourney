package merail.life.design.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.SubcomposeAsyncImage
import merail.life.core.extensions.createMediaRequest
import merail.life.design.MejourneyTheme

@Composable
fun ContentImage(
    url: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    MejourneyTheme {
        SubcomposeAsyncImage(
            model = LocalContext.current.createMediaRequest(url),
            contentDescription = null,
            loading = {
                Loading()
            },
            contentScale = contentScale,
            modifier = modifier,
        )
    }
}