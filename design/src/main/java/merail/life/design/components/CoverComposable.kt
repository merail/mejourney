package merail.life.design.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import merail.life.design.extensions.createMediaRequest

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CoverImage(
    id: String,
    url: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillWidth,
    onLoadingSuccess: () -> Unit = {},
    navigateTo: (String) -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    SubcomposeAsyncImage(
        model = LocalContext.current.createMediaRequest(url),
        contentDescription = null,
        contentScale = contentScale,
        loading = {
            ImageLoading()
        },
        onSuccess = {
            onLoadingSuccess.invoke()
        },
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onLongClick = {
                    onLongClick.invoke()
                },
                onClick = {
                    navigateTo.invoke(id)
                },
            ),
    )
}

@Composable
private fun ImageLoading() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(64.dp),
    ) {
        CircularProgressIndicator()
    }
}