package merail.life.home.main

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import merail.life.core.INotificationsPermissionRequester
import merail.life.core.NavigationDestination
import merail.life.core.extensions.activity
import merail.life.core.extensions.isNavigationBarEnabled
import merail.life.core.extensions.rerunApp
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
import merail.life.home.model.HomeItem
import merail.life.home.model.SelectorFilter
import merail.life.home.model.TabFilter
import merail.life.home.model.toModel

object HomeDestination : NavigationDestination {
    override val route = "home"
}

@Composable
fun HomeScreen(
    onError: (Throwable?) -> Unit,
    navigateToSelector: (SelectorFilterType) -> Unit,
    navigateToContent: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val state = viewModel.uiState.collectAsState().value

    when (state) {
        is HomeUiState.UnauthorizedException -> LaunchedEffect(null) {
            context.rerunApp()
        }
        is HomeUiState.CommonError -> LaunchedEffect(null) {
            onError(state.exception)
        }
        is HomeUiState.Success -> LaunchedEffect(null) {
            (context.activity as? INotificationsPermissionRequester)?.requestPermission()
        }
        is HomeUiState.None,
        is HomeUiState.Loading,
        -> Unit
    }

    if (state is HomeUiState.Success) {
        (LocalContext.current.activity as? INotificationsPermissionRequester)?.requestPermission()
    }

    val onTabClick: (TabFilter) -> Unit = remember {
        { tabFilter: TabFilter ->
            viewModel.getHomeItems(tabFilter)
        }
    }

    val onSelectorClick = remember {
        { selectorFilter: SelectorFilter ->
            navigateToSelector(selectorFilter.toModel())
        }
    }

    Content(
        state = state,
        navigateToSelector = onSelectorClick,
        navigateToContent = navigateToContent,
        onTabClick = onTabClick,
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
        HomeLoader(state)

        var tabFilter by rememberSaveable {
            mutableStateOf(TabFilter.COMMON)
        }

        TabsContent(
            tabFilter = tabFilter,
            items = state.items,
            navigateToSelector = navigateToSelector,
            navigateToContent = navigateToContent,
        )

        val onTabClickInternal = remember {
            { it: TabFilter ->
                tabFilter = it
                onTabClick(it)
            }
        }

        HomeTabs(
            onTabClick = onTabClickInternal,
        )
    }
}

@Composable
private fun HomeLoader(
    state: HomeUiState,
) {
    if (state.items.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            CircularProgressIndicator()
        }
    } else {
        AnimatedVisibility(
            visible = state is HomeUiState.Loading,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 32.dp,
                        bottom = 16.dp,
                    ),
            ) {
                CircularProgressIndicator(
                    strokeWidth = 3.dp,
                    modifier = Modifier
                        .size(24.dp),
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.TabsContent(
    tabFilter: TabFilter,
    items: ImmutableList<HomeItem>,
    navigateToSelector: (SelectorFilter) -> Unit,
    navigateToContent: (String) -> Unit,
) {
    val navigateToSelectorInternal = items.navigateToContentInternal {
        navigateToSelector(
            when (tabFilter) {
                TabFilter.YEAR -> SelectorFilter.Year(year)
                TabFilter.COUNTRY -> SelectorFilter.Country(country)
                else -> SelectorFilter.Place(place)
            }
        )
    }

    when (tabFilter) {
        TabFilter.YEAR -> YearsList(
            items = items,
            navigateToContent = navigateToSelectorInternal,
        )
        TabFilter.COUNTRY -> CountriesList(
            items = items,
            navigateToContent = navigateToSelectorInternal,
        )
        TabFilter.PLACE -> PlacesList(
            items = items,
            navigateToContent = navigateToSelectorInternal,
        )
        TabFilter.COMMON -> CommonList(
            items = items,
            navigateToContent = navigateToContent,
        )
    }
}

private fun List<HomeItem>.navigateToContentInternal(
    block: HomeItem.() -> Unit,
): (String) -> Unit = { id: String -> find { item ->
        item.id == id
    }?.let {
        block(it)
    }
}


private val list = persistentListOf(
    Pair(TabFilter.YEAR, R.string.main_tab_years_name),
    Pair(TabFilter.COUNTRY, R.string.main_tab_countries_name),
    Pair(TabFilter.PLACE, R.string.main_tab_places_name),
    Pair(TabFilter.COMMON, R.string.main_tab_all_name),
)

@Composable
private fun HomeTabs(
    onTabClick: (TabFilter) -> Unit,
) {
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
                bottom = if (LocalContext.current.isNavigationBarEnabled) {
                    56.dp
                } else {
                    24.dp
                },
            )
            .clip(RoundedCornerShape(64)),
    ) {
        list.forEachIndexed { index, tabElement ->
            with(tabElement) {
                HomeTab(
                    selectedIndex = selectedIndex,
                    index = index,
                    onTabClick = onTabClick,
                )
            }
        }
    }
}

@Composable
private fun Pair<TabFilter, Int>.HomeTab(
    selectedIndex: MutableState<Int>,
    index: Int,
    onTabClick: (TabFilter) -> Unit,
) {
    val onTabClickInternal = remember {
        {
            if (selectedIndex.value != index) {
                selectedIndex.value = index
                onTabClick(first)
            }
        }
    }

    Box(
        modifier = Modifier
            .height(40.dp)
            .padding(4.dp),
    ) {
        val isSelected = selectedIndex.value == index
        Tab(
            selected = isSelected,
            onClick = onTabClickInternal,
            text = {
                HomeTabText(
                    textRes = second,
                    color = if (selectedIndex.value == index) {
                        MejourneyTheme.colors.textPrimary
                    } else {
                        MejourneyTheme.colors.unselectedTabTextColor
                    },
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

@Composable
private fun HomeTabText(
    @StringRes textRes: Int,
    color: Color,
) {
    Text(
        text = stringResource(textRes),
        color = color,
        style = MejourneyTheme.typography.labelLarge,
        modifier = Modifier
            .wrapContentWidth(
                unbounded = true,
            ),
    )
}