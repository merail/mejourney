package merail.life.mejourney.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
) {
    Column(
        modifier = modifier
            .animateContentSize(),
    ) {
        val isImageLongClicked = remember {
            mutableStateOf(false)
        }

        SubcomposeAsyncImage(
            model = item.url,
            contentDescription = null,
            loading = {
                ImageLoading()
            },
            modifier = Modifier
                .combinedClickable(
                    onLongClick = {
                        isImageLongClicked.value = isImageLongClicked.value.not()
                    },
                    onClick = {
                        navigateToEvent.invoke()
                    },
                ),
        )

        AnimatedVisibility(
            visible = isImageLongClicked.value,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp),
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
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