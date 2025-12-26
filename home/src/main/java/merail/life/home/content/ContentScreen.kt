package merail.life.home.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import merail.life.design.MejourneyTheme
import merail.life.design.cardColors
import merail.life.design.components.ContentImage
import merail.life.design.components.Loading
import merail.life.design.extensions.robustSystemBarsPadding
import merail.life.domain.TestTags
import merail.life.home.content.state.ContentLoadingState
import merail.life.home.model.ContentItem
import merail.life.home.model.IMAGE_DELIMITER
import merail.life.home.model.splitWithImages

@Composable
internal fun ContentScreen(
    navigateToError: (Throwable?) -> Unit,
    viewModel: ContentViewModel = hiltViewModel(),
) {
    val state by viewModel.contentLoadingState.collectAsStateWithLifecycle()

    when (val currentState = state) {
        is ContentLoadingState.Loading -> Loading()
        is ContentLoadingState.Error -> LaunchedEffect(currentState) {
            navigateToError(currentState.exception)
        }
        is ContentLoadingState.Success -> Content(currentState.item)
    }
}

@Composable
private fun Content(
    item: ContentItem,
) {
    val contentParts = remember(item.id) { item.splitWithImages() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .robustSystemBarsPadding()
            .padding(
                horizontal = 24.dp,
            )
            .verticalScroll(rememberScrollState())
            .testTag(TestTags.CONTENT_SCREEN_CONTAINER),
    ) {
        Text(
            text = item.title,
            style = MejourneyTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(
                    horizontal = 12.dp,
                ),
        )

        var index = 0
        contentParts.forEach { text ->
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