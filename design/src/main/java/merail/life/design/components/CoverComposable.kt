package merail.life.design.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.SubcomposeAsyncImage
import com.valentinilk.shimmer.shimmer
import merail.life.design.MejourneyTheme
import merail.life.design.extensions.createMediaRequest
import merail.life.design.shimmerColor

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
    SubcomposeAsyncImage(
        model = LocalContext.current.createMediaRequest(url),
        contentDescription = null,
        contentScale = contentScale,
        loading = {
            if (loading == null) {
                ImageLoading()
            } else {
                loading()
            }
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
            .shimmer(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MejourneyTheme.colors.shimmerColor)
        )
    }
}