package merail.life.home.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.collections.immutable.ImmutableList
import merail.life.core.NavigationDestination
import merail.life.design.components.ErrorMessage
import merail.life.design.components.Loading
import merail.life.design.selectedTabColor
import merail.life.design.tabsContainerColor
import merail.life.design.unselectedTabTextColor
import merail.life.firebase.data.model.HomeFilterType
import merail.life.home.R
import merail.life.home.home.tabs.CommonList
import merail.life.home.home.tabs.CountriesList
import merail.life.home.home.tabs.PlacesList
import merail.life.home.home.tabs.YearsList
import merail.life.home.model.HomeItem
import merail.life.home.model.TabFilter
import merail.life.home.model.toModel

object HomeDestination : NavigationDestination {
    override val route = "home"
}

@Composable
fun HomeScreen(
    navigateToSelector: (HomeFilterType) -> Unit,
    navigateToContent: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel<HomeViewModel>(),
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is HomeUiState.Loading -> Loading()
        is HomeUiState.Error -> ErrorMessage(uiState.exception.message.orEmpty())
        is HomeUiState.Success -> Content(
            items = uiState.items,
            navigateToSelector = {
                navigateToSelector.invoke(it.toModel())
            },
            navigateToContent = navigateToContent,
            onTabClick = {
                viewModel.getItems(it)
            },
        )
    }
}

@Composable
private fun Content(
    items: ImmutableList<HomeItem>,
    navigateToSelector: (TabFilter) -> Unit = {},
    navigateToContent: (String) -> Unit = {},
    onTabClick: (TabFilter) -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        var tabFilter by rememberSaveable {
            mutableStateOf(TabFilter.COMMON)
        }

        when (tabFilter) {
            TabFilter.YEAR -> YearsList(
                items = items,
                navigateToContent = {
                    if (items.size == 1) {
                        navigateToContent.invoke(it)
                    } else {
                        navigateToSelector.invoke(TabFilter.YEAR)
                    }
                },
            )
            TabFilter.COUNTRY -> CountriesList(
                items = items,
                navigateToContent = {
                    if (items.size == 1) {
                        navigateToContent.invoke(it)
                    } else {
                        navigateToSelector.invoke(TabFilter.COUNTRY)
                    }
                },
            )
            TabFilter.PLACE -> PlacesList(
                items = items,
                navigateToContent = {
                    if (items.size == 1) {
                        navigateToContent.invoke(it)
                    } else {
                        navigateToSelector.invoke(TabFilter.COUNTRY)
                    }
                },
            )
            TabFilter.COMMON -> CommonList(
                items = items,
                navigateToContent = navigateToContent,
            )
        }

        HomeTabs(
            onTabClick = {
                tabFilter = it
                onTabClick.invoke(it)
            },
        )
    }
}

@Composable
private fun HomeTabs(
    onTabClick: (TabFilter) -> Unit,
) {
    val list = listOf(
        Pair(TabFilter.YEAR, stringResource(R.string.main_tab_years_name)),
        Pair(TabFilter.COUNTRY, stringResource(R.string.main_tab_countries_name)),
        Pair(TabFilter.PLACE, stringResource(R.string.main_tab_places_name)),
        Pair(TabFilter.COMMON, stringResource(R.string.main_tab_all_name)),
    )
    val selectedIndex = rememberSaveable {
        mutableIntStateOf(list.size - 1)
    }
    TabRow(
        selectedTabIndex = selectedIndex.intValue,
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
        list.forEachIndexed { index, tabElement ->
            with(tabElement) {
                HomeTab(
                    selectedIndex = selectedIndex,
                    index = index,
                    onTabClick = onTabClick
                )
            }
        }
    }
}

@Composable
private fun Pair<TabFilter, String>.HomeTab(
    selectedIndex: MutableState<Int>,
    index: Int,
    onTabClick: (TabFilter) -> Unit,
) {
    Box(
        modifier = Modifier
            .height(40.dp)
            .padding(4.dp),
    ) {
        val isSelected = selectedIndex.value == index
        Tab(
            selected = isSelected,
            onClick = {
                selectedIndex.value = index
                onTabClick.invoke(first)
            },
            text = {
                Text(
                    text = second,
                    color = if (selectedIndex.value == index) {
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
                    color = if (selectedIndex.value == index) {
                        selectedTabColor
                    } else {
                        tabsContainerColor
                    },
                ),
        )
    }
}