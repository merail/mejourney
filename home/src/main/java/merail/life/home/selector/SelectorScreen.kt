package merail.life.home.selector

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import merail.life.core.extensions.isSingle
import merail.life.design.MejourneyTheme
import merail.life.design.cardColors
import merail.life.design.components.CoverImage
import merail.life.design.components.ImageLoading
import merail.life.design.components.Loading
import merail.life.design.extensions.robustSystemBarsPadding
import merail.life.domain.TestTags
import merail.life.home.model.HomeItem
import merail.life.home.selector.state.SelectionLoadingState

@Composable
internal fun SelectorScreen(
    onError: (Throwable?) -> Unit,
    navigateToContent: (String) -> Unit,
    navigateToContentImmediately: (String) -> Unit,
    viewModel: SelectorViewModel = hiltViewModel(),
) {
    val state by viewModel.selectionLoadingState.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        val currentState = state
        if (currentState is SelectionLoadingState.Success && currentState.items.isSingle) {
            navigateToContentImmediately(currentState.items.first().id)
        }
    }

    when (val currentState = state) {
        is SelectionLoadingState.Loading -> Loading()
        is SelectionLoadingState.Error -> LaunchedEffect(currentState) {
            onError(currentState.exception)
        }
        is SelectionLoadingState.Success -> {
            if (currentState.items.isSingle.not()) {
                Content(
                    items = currentState.items,
                    navigateToContent = navigateToContent,
                )
            }
        }
    }
}

@Composable
private fun Content(
    items: ImmutableList<HomeItem>,
    navigateToContent: (String) -> Unit = {},
) {
    val pagerState = rememberPagerState(
        pageCount = {
            items.size
        },
    )

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(10.dp),
        modifier = Modifier
            .testTag(TestTags.SELECTOR_SCREEN_CONTAINER),
    ) { page ->
        SelectorItem(
            item = items[page],
            navigateToContent = navigateToContent,
        )
    }
}

@Composable
private fun SelectorItem(
    item: HomeItem,
    navigateToContent: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .robustSystemBarsPadding()
            .padding(
                horizontal = 4.dp,
            )
            .clip(
                shape = RoundedCornerShape(12.dp),
            )
            .clickable {
                navigateToContent(item.id)
            },
    ) {
        Card(
            colors = MejourneyTheme.colors.cardColors,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            CoverImage(
                id = item.id,
                url = item.imageUrl,
                navigateTo = navigateToContent,
                contentScale = ContentScale.Crop,
                loading = {
                    ImageLoading(Modifier.height(640.dp))
                },
                modifier = Modifier
                    .fillMaxSize(),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight(
                    fraction = 0.24f,
                ),
        ) {
            Text(
                text = item.title,
                style = MejourneyTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            )
            Text(
                text = item.description,
                style = MejourneyTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 12.dp,
                    ),
            )
        }
    }
}