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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.collections.immutable.ImmutableList
import merail.life.core.NavigationDestination
import merail.life.design.components.Error
import merail.life.design.components.ItemsParameterProvider
import merail.life.design.components.Loading
import merail.life.design.selectedTabColor
import merail.life.design.tabsContainerColor
import merail.life.design.unselectedTabTextColor
import merail.life.firebase.data.model.HomeFilterType
import merail.life.firebase.data.model.HomeModel
import merail.life.home.R
import merail.life.home.home.tabs.CommonList
import merail.life.home.home.tabs.CountriesList
import merail.life.home.home.tabs.PlacesList
import merail.life.home.home.tabs.YearsList

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
        is HomeUiState.Error -> Error(uiState.exception.message.orEmpty())
        is HomeUiState.Success -> Content(
            items = uiState.items,
            navigateToSelector = navigateToSelector,
            navigateToContent = navigateToContent,
            onTabClick = {
                viewModel.getItems(it)
            },
        )
    }
}

@Preview
@Composable
private fun Content(
    @PreviewParameter(ItemsParameterProvider::class) items: ImmutableList<HomeModel>,
    navigateToSelector: (HomeFilterType) -> Unit = {},
    navigateToContent: (String) -> Unit = {},
    onTabClick: (HomeFilterType) -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        var tabFilter by rememberSaveable {
            mutableStateOf(HomeFilterType.COMMON)
        }

        when (tabFilter) {
            HomeFilterType.YEAR -> YearsList(
                items = items,
                navigateToContent = {
                    if (items.size == 1) {
                        navigateToContent.invoke(it)
                    } else {
                        navigateToSelector.invoke(HomeFilterType.YEAR)
                    }
                },
            )
            HomeFilterType.COUNTRY -> CountriesList(
                items = items,
                navigateToContent = {
                    if (items.size == 1) {
                        navigateToContent.invoke(it)
                    } else {
                        navigateToSelector.invoke(HomeFilterType.COUNTRY)
                    }
                },
            )
            HomeFilterType.PLACE -> PlacesList(
                items = items,
                navigateToContent = {
                    if (items.size == 1) {
                        navigateToContent.invoke(it)
                    } else {
                        navigateToSelector.invoke(HomeFilterType.COUNTRY)
                    }
                },
            )
            HomeFilterType.COMMON -> CommonList(
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
    onTabClick: (HomeFilterType) -> Unit,
) {
    val list = listOf(
        Pair(HomeFilterType.YEAR, stringResource(R.string.main_tab_years_name)),
        Pair(HomeFilterType.COUNTRY, stringResource(R.string.main_tab_countries_name)),
        Pair(HomeFilterType.PLACE, stringResource(R.string.main_tab_places_name)),
        Pair(HomeFilterType.COMMON, stringResource(R.string.main_tab_all_name)),
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
private fun Pair<HomeFilterType, String>.HomeTab(
    selectedIndex: MutableState<Int>,
    index: Int,
    onTabClick: (HomeFilterType) -> Unit,
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