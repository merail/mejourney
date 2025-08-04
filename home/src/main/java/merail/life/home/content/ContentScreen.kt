package merail.life.home.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import merail.life.design.MejourneyTheme
import merail.life.design.cardColors
import merail.life.design.components.ContentImage
import merail.life.design.components.Loading
import merail.life.home.model.ContentItem
import merail.life.home.model.IMAGE_DELIMITER
import merail.life.home.model.splitWithImages

@Composable
fun ContentContainer(
    navigateToError: (Throwable?) -> Unit,
) = ContentScreen(
    navigateToError = navigateToError,
)

@Composable
internal fun ContentScreen(
    navigateToError: (Throwable?) -> Unit,
    viewModel: ContentViewModel = hiltViewModel<ContentViewModel>(),
) {
    when (val uiState = viewModel.contentLoadingState.collectAsState().value) {
        is ContentLoadingState.Loading -> Loading()
        is ContentLoadingState.Error -> LaunchedEffect(null) {
            navigateToError(uiState.exception)
        }
        is ContentLoadingState.Success -> Content(uiState.item)
    }
}

@Composable
private fun Content(
    item: ContentItem,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(
                start = 24.dp,
                top = 24.dp,
                end = 24.dp,
            )
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = item.title,
            style = MejourneyTheme.typography.titleLarge,
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
                    style = MejourneyTheme.typography.bodyLarge,
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
        colors = MejourneyTheme.colors.cardColors,
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