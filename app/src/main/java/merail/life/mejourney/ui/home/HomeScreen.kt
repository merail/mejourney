package merail.life.mejourney.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import kotlinx.collections.immutable.ImmutableList
import merail.life.mejourney.R
import merail.life.mejourney.data.HomeItem
import merail.life.mejourney.navigation.NavigationDestination
import merail.life.mejourney.ui.AppViewModelProvider
import merail.life.mejourney.ui.theme.selectedTabColor
import merail.life.mejourney.ui.theme.tabsContainerColor
import merail.life.mejourney.ui.theme.unselectedTabTextColor

object HomeDestination : NavigationDestination {
    override val route = "home"
}

@Composable
fun HomeScreen(
    navigateToEvent: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is HomeUiState.Loading -> Loading()
        is HomeUiState.Error -> Error(uiState.exception.message.orEmpty())
        is HomeUiState.Success -> Content(
            items = uiState.items,
            navigateToEvent = navigateToEvent,
        )
    }
}

@Composable
private fun Loading() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Error(
    errorMessage: String,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Text(
            text = errorMessage,
            color = Color.Red,
        )
    }
}

@Composable
private fun Content(
    items: ImmutableList<HomeItem>,
    navigateToEvent: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        MainList(
            items = items,
            navigateToEvent = navigateToEvent,
        )

        MainTabs()
    }
}

@Composable
private fun ColumnScope.MainList(
    items: ImmutableList<HomeItem>,
    navigateToEvent: () -> Unit,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .weight(1f)
            .padding(4.dp),
    ) {
        items(items) {
            Cover(
                item = it,
                navigateToEvent = navigateToEvent,
            )
        }
    }
}

@Composable
private fun MainTabs() {
    val list = listOf(
        stringResource(R.string.main_tab_years_name),
        stringResource(R.string.main_tab_countries_name),
        stringResource(R.string.main_tab_places_name),
        stringResource(R.string.main_tab_all_name),
    )
    var selectedIndex by remember {
        mutableIntStateOf(list.size - 1)
    }
    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = tabsContainerColor,
        indicator = {},
        divider = {},
        modifier = Modifier
            .padding(
                start = 24.dp,
                top = 8.dp,
                end = 24.dp,
                bottom = 24.dp,
            )
            .clip(RoundedCornerShape(64)),
    ) {
        list.forEachIndexed { index, text ->
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .padding(4.dp),
            ) {
                val selected = selectedIndex == index
                Tab(
                    selected = selected,
                    onClick = {
                        selectedIndex = index
                    },
                    text = {
                        Text(
                            text = text,
                            color = if (selectedIndex == index) {
                                Color.White
                            } else {
                                unselectedTabTextColor
                            },
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier
                                .wrapContentWidth(
                                    unbounded = true,
                                ),
                        )
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(64))
                        .background(
                            color = if (selectedIndex == index) {
                                selectedTabColor
                            } else {
                                tabsContainerColor
                            },
                        ),
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Cover(
    item: HomeItem,
    navigateToEvent: () -> Unit,
) {
    Column(
        modifier = Modifier
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
