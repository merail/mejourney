package merail.life.design.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import merail.life.design.extensions.createMediaRequest

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CoverImage(
    id: String,
    url: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillWidth,
    loading: (@Composable () -> Unit)? = null,
    onLoadingSuccess: () -> Unit = {},
    navigateTo: (String) -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    val onSuccess = remember {
        { _: AsyncImagePainter.State.Success ->
            onLoadingSuccess()
        }
    }
    val onClick = remember {
        {
            navigateTo(id)
        }
    }
    SubcomposeAsyncImage(
        model = LocalContext.current.createMediaRequest(url),
        contentDescription = null,
        contentScale = contentScale,
        loading = {
            if (loading == null) {
                ImageLoading(Modifier.height(256.dp))
            } else {
                loading()
            }
        },
        onSuccess = onSuccess,
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onLongClick = onLongClick,
                onClick = onClick,
            ),
    )
}