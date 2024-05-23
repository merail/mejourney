package merail.life.mejourney.ui.selector

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.collections.immutable.ImmutableList
import merail.life.mejourney.data.HomeItem
import merail.life.mejourney.navigation.NavigationDestination
import merail.life.mejourney.ui.AppViewModelProvider
import merail.life.mejourney.ui.common.Cover
import merail.life.mejourney.ui.common.Error
import merail.life.mejourney.ui.common.ItemsParameterProvider
import merail.life.mejourney.ui.common.Loading
import merail.life.mejourney.ui.theme.MejourneyTheme

object SelectorDestination : NavigationDestination {
    override val route = "selector"

    const val TAB_FILTER_ARG = "tabFilter"

    val routeWithArgs = "$route/{$TAB_FILTER_ARG}"
}

@Composable
fun SelectorScreen(
    viewModel: SelectorViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is SelectorUiState.Loading -> Loading()
        is SelectorUiState.Error -> Error(uiState.exception.message.orEmpty())
        is SelectorUiState.Success -> Content(uiState.items)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun Content(
    @PreviewParameter(ItemsParameterProvider::class) items: ImmutableList<HomeItem>,
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
            SelectorItem(items[page])
        }
    }
}

@Composable
private fun SelectorItem(
    item: HomeItem,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(36.dp),
    ) {
        Card(
            colors = CardDefaults.cardColors().copy(
                containerColor = Color.Black,
            ),
        ) {
            Cover(
                item = item,
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