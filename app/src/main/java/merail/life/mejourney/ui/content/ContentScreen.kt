package merail.life.mejourney.ui.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import merail.life.firebase.data.model.ContentItem
import merail.life.firebase.data.model.splitText
import merail.life.mejourney.navigation.NavigationDestination
import merail.life.mejourney.ui.common.Error
import merail.life.mejourney.ui.common.Loading

object ContentDestination : NavigationDestination {
    override val route = "content"

    const val CONTENT_ID_ARG = "contentId"

    val routeWithArgs = "$route/{$CONTENT_ID_ARG}"
}

@Composable
fun ContentScreen(
    viewModel: ContentViewModel = hiltViewModel<ContentViewModel>(),
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is ContentUiState.Loading -> Loading()
        is ContentUiState.Error -> Error(uiState.exception.message.orEmpty())
        is ContentUiState.Success -> Content(uiState.item)
    }
}

@Composable
private fun Content(
    item: ContentItem,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(12.dp),
        )

        item.splitText().forEachIndexed { index, text ->
            Text(
                text = text,
            )

            if (index < item.imagesUrls.size) {
                SubcomposeAsyncImage(
                    model = item.imagesUrls[index],
                    contentDescription = null,
//                loading = {
//                    if (LocalInspectionMode.current) {
//                        PreviewPlaceholder()
//                    } else {
//                        ImageLoading()
//                    }
//                },
//                onSuccess = {
//                    onLoadingSuccess.invoke()
//                },
//                modifier = modifier
//                    .combinedClickable(
//                        onLongClick = {
//                            onLongClick.invoke()
//                        },
//                        onClick = {
//                            navigateToContent.invoke(item.id)
//                        },
//                    ),
                )
            }
        }
    }
}