package merail.life.home.selector

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.collections.immutable.ImmutableList
import merail.life.core.NavigationDestination
import merail.life.design.MejourneyTheme
import merail.life.design.cardColors
import merail.life.design.components.CoverImage
import merail.life.design.components.Loading
import merail.life.home.model.HomeItem

object SelectorDestination : NavigationDestination {
    override val route = "selector"

    const val SELECTOR_FILTER_ARG = "selectorFilter"

    val routeWithArgs = "$route/{$SELECTOR_FILTER_ARG}"
}

@Composable
fun SelectorScreen(
    onError: (Throwable?) -> Unit,
    navigateToContent: (String) -> Unit,
    viewModel: SelectorViewModel = hiltViewModel<SelectorViewModel>(),
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is SelectorUiState.None -> Unit
        is SelectorUiState.Loading -> Loading()
        is SelectorUiState.Error -> onError(uiState.exception)
        is SelectorUiState.Success -> Content(
            items = uiState.items,
            navigateToContent = navigateToContent,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
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
                top = 32.dp,
                end = 4.dp,
                bottom = 20.dp,
            )
            .clickable {
                navigateToContent.invoke(item.id)
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
                navigateTo = {
                    navigateToContent.invoke(item.id)
                },
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .fillMaxSize(),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight(0.18f),
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