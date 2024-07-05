package merail.life.home.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import merail.life.core.NavigationDestination
import merail.life.data.model.SelectorFilterType
import merail.life.design.MejourneyTheme
import merail.life.design.selectedTabColor
import merail.life.design.tabsContainerColor
import merail.life.design.unselectedTabColor
import merail.life.design.unselectedTabTextColor
import merail.life.home.R
import merail.life.home.main.tabs.CommonList
import merail.life.home.main.tabs.CountriesList
import merail.life.home.main.tabs.PlacesList
import merail.life.home.main.tabs.YearsList
import merail.life.home.model.SelectorFilter
import merail.life.home.model.TabFilter
import merail.life.home.model.toModel

object HomeDestination : NavigationDestination {
    override val route = "home"
}

@Composable
fun HomeScreen(
    navigateToSelector: (SelectorFilterType) -> Unit,
    navigateToContent: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel<HomeViewModel>(),
) {
    Content(
        state = viewModel.uiState.collectAsState().value,
        navigateToSelector = { selectorFilter ->
            navigateToSelector.invoke(selectorFilter.toModel())
        },
        navigateToContent = navigateToContent,
        onTabClick = {
            viewModel.getHomeItems(it)
        },
    )
}

@Composable
private fun Content(
    state: HomeUiState,
    navigateToSelector: (SelectorFilter) -> Unit,
    navigateToContent: (String) -> Unit = {},
    onTabClick: (TabFilter) -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        when (state) {
            is HomeUiState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 64.dp,
                        ),
                ) {
                    CircularProgressIndicator()
                }
            }
            is HomeUiState.Error -> Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 64.dp,
                    ),
            ) {
                Text(
                    text = state.exception?.message.orEmpty(),
                    color = MejourneyTheme.colors.textNegative,
                )
            }
            else -> Unit
        }

        var tabFilter by rememberSaveable {
            mutableStateOf(TabFilter.COMMON)
        }

        when (tabFilter) {
            TabFilter.YEAR -> YearsList(
                items = state.items,
                navigateToContent = {
                    if (state.items.size == 1) {
                        navigateToContent.invoke(it)
                    } else {
                        state.items.find { item ->
                            item.id == it
                        }?.run {
                            navigateToSelector.invoke(SelectorFilter.Year(year))
                        }
                    }
                },
            )
            TabFilter.COUNTRY -> CountriesList(
                items = state.items,
                navigateToContent = {
                    if (state.items.size == 1) {
                        navigateToContent.invoke(it)
                    } else {
                        state.items.find { item ->
                            item.id == it
                        }?.run {
                            navigateToSelector.invoke(SelectorFilter.Country(country))
                        }
                    }
                },
            )
            TabFilter.PLACE -> PlacesList(
                items = state.items,
                navigateToContent = {
                    if (state.items.size == 1) {
                        navigateToContent.invoke(it)
                    } else {
                        state.items.find { item ->
                            item.id == it
                        }?.run {
                            navigateToSelector.invoke(SelectorFilter.Place(place))
                        }
                    }
                },
            )
            TabFilter.COMMON -> CommonList(
                items = state.items,
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
        containerColor = MejourneyTheme.colors.tabsContainerColor,
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
                        MejourneyTheme.colors.textPrimary
                    } else {
                        MejourneyTheme.colors.unselectedTabTextColor
                    },
                    style = MejourneyTheme.typography.labelLarge,
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
                        MejourneyTheme.colors.selectedTabColor
                    } else {
                        MejourneyTheme.colors.unselectedTabColor
                    },
                ),
        )
    }
}