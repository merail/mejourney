package merail.life.design.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import merail.life.design.MejourneyTheme
import merail.life.design.R
import merail.life.firebase.data.model.HomeModel

@Preview
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Cover(
    @PreviewParameter(ItemParameterProvider::class) item: HomeModel,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    onLoadingSuccess: () -> Unit = {},
    navigateToContent: (String) -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    MejourneyTheme {
        SubcomposeAsyncImage(
            model = item.url,
            contentDescription = null,
            contentScale = contentScale,
            loading = {
                if (LocalInspectionMode.current) {
                    PreviewPlaceholder()
                } else {
                    ImageLoading()
                }
            },
            onSuccess = {
                onLoadingSuccess.invoke()
            },
            modifier = modifier
                .combinedClickable(
                    onLongClick = {
                        onLongClick.invoke()
                    },
                    onClick = {
                        navigateToContent.invoke(item.id)
                    },
                ),
        )
    }
}

@Composable
private fun PreviewPlaceholder() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Image(
            painter = painterResource(R.drawable.preview_placeholder),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
        )
    }
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