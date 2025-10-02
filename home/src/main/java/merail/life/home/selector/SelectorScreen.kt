package merail.life.home.selector

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.collections.immutable.ImmutableList
import merail.life.core.extensions.isSingle
import merail.life.design.MejourneyTheme
import merail.life.design.cardColors
import merail.life.design.components.CoverImage
import merail.life.design.components.ImageLoading
import merail.life.design.components.Loading
import merail.life.design.extensions.pureStatusBarHeight
import merail.life.home.model.HomeItem
import merail.life.home.selector.state.SelectionLoadingState

@Composable
internal fun SelectorScreen(
    onError: (Throwable?) -> Unit,
    navigateToContent: (String) -> Unit,
    navigateToContentImmediately: (String) -> Unit,
    viewModel: SelectorViewModel = hiltViewModel<SelectorViewModel>(),
) {
    when (val uiState = viewModel.selectionLoadingState.collectAsState().value) {
        is SelectionLoadingState.Loading -> Loading()
        is SelectionLoadingState.Error -> LaunchedEffect(null) {
            onError(uiState.exception)
        }
        is SelectionLoadingState.Success -> if (uiState.items.isSingle) {
            navigateToContentImmediately(uiState.items.first().id)
        } else {
            Content(
                items =uiState.items,
                navigateToContent = navigateToContent,
            )
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
            .padding(
                start = 4.dp,
                top = pureStatusBarHeight(),
                end = 4.dp,
            )
            .navigationBarsPadding()
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
                url = item.url,
                navigateTo =  {
                    navigateToContent(it)
                },
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