package merail.life.home.selector

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import kotlinx.collections.immutable.ImmutableList
import merail.life.core.NavigationDestination
import merail.life.design.MejourneyTheme
import merail.life.design.components.Cover
import merail.life.design.components.Error
import merail.life.design.components.Loading
import merail.life.home.model.HomeItem

object SelectorDestination : NavigationDestination {
    override val route = "selector"

    const val TAB_FILTER_ARG = "tabFilter"

    val routeWithArgs = "$route/{$TAB_FILTER_ARG}"
}

@Composable
fun SelectorScreen(
    navigateToContent: (String) -> Unit,
    viewModel: SelectorViewModel = hiltViewModel<SelectorViewModel>(),
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is SelectorUiState.Loading -> Loading()
        is SelectorUiState.Error -> Error(uiState.exception.message.orEmpty())
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
    MejourneyTheme {
        val pagerState = rememberPagerState(
            pageCount = {
                items.size
            },
        )
        HorizontalPager(
            state = pagerState,
        ) { page ->
            SelectorItem(
                item = items[page],
                navigateToContent = navigateToContent,
            )
        }
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
            .fillMaxSize()
            .padding(36.dp)
            .clickable {
                navigateToContent.invoke(item.id)
            },
    ) {
        Card(
            colors = CardDefaults.cardColors().copy(
                containerColor = Color.Black,
            ),
        ) {
            Cover(
                id = item.id,
                url = item.url,
                navigateToContent = {
                    navigateToContent.invoke(item.id)
                },
            )
        }

        Text(
            text = item.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(
                    vertical = 12.dp,
                ),
        )

        Text(
            text = item.description,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
        )
    }
}