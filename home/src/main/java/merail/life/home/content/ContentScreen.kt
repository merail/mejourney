package merail.life.home.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import merail.life.core.NavigationDestination
import merail.life.design.components.ContentImage
import merail.life.design.components.ErrorMessage
import merail.life.design.components.Loading
import merail.life.home.model.ContentItem
import merail.life.home.model.IMAGE_DELIMITER
import merail.life.home.model.splitWithImages

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
        is ContentUiState.Error -> ErrorMessage(uiState.exception?.message.orEmpty())
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

        var index = 0
        item.splitWithImages().forEach { text ->
            if (text == IMAGE_DELIMITER) {
                if (index < item.imagesUrls.size) {
                    ContentImage(
                        index = index,
                        item = item,
                    )
                }
                index++
            } else {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(
                            vertical = 12.dp,
                        ),
                )
            }
        }
    }
}

@Composable
private fun ContentImage(
    index: Int,
    item: ContentItem,
) {
    Card(
        colors = CardDefaults.cardColors().copy(
            containerColor = Color.Black,
        ),
        modifier = Modifier
            .wrapContentSize()
            .padding(
                vertical = 12.dp,
            ),
    ) {
        ContentImage(
            url = item.imagesUrls[index],
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}