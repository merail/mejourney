package merail.life.mejourney.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import merail.life.mejourney.data.HomeItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Cover(
    item: HomeItem,
    navigateToEvent: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: () -> Unit = {},
) {
    SubcomposeAsyncImage(
        model = item.url,
        contentDescription = null,
        loading = {
            ImageLoading()
        },
        modifier = modifier
            .combinedClickable(
                onLongClick = {
                    onLongClick.invoke()
                },
                onClick = {
                    navigateToEvent.invoke()
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