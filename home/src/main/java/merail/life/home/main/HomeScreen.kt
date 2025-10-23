package merail.life.home.main

import androidx.activity.compose.LocalActivity
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
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.idapgroup.snowfall.snowfall
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import merail.life.core.extensions.rerunApp
import merail.life.core.permissions.NotificationsPermissionRequester
import merail.life.data.api.model.SelectorFilterType
import merail.life.design.MejourneyTheme
import merail.life.design.extensions.pureStatusBarHeight
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

@Composable
internal fun HomeScreen(
    onError: (Throwable?) -> Unit,
    navigateToSelector: (SelectorFilterType) -> Unit,
    navigateToContent: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val activity = LocalActivity.current

    val state = viewModel.state.collectAsState().value

    when (state) {
        is HomeLoadingState.UnauthorizedException -> LaunchedEffect(null) {
            activity?.rerunApp()
        }
        is HomeLoadingState.CommonError -> LaunchedEffect(null) {
            onError(state.exception)
        }
        is HomeLoadingState.Success -> LaunchedEffect(null) {
            (activity as? NotificationsPermissionRequester)?.requestPermission()
        }
        is HomeLoadingState.Loading,
        -> Unit
    }

    Content(
        state = state,
        isSnowfallEnabled = viewModel.isSnowfallEnabled,
        navigateToSelector = {
            navigateToSelector(it.toModel())
        },
        navigateToContent = navigateToContent,
        onTabClick = viewModel::getHomeItems,
    )
}

@Composable
private fun Content(
    state: HomeLoadingState,
    isSnowfallEnabled: Boolean,
    navigateToSelector: (SelectorFilter) -> Unit,
    navigateToContent: (String) -> Unit = {},
    onTabClick: (TabFilter) -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .run {
                if (isSnowfallEnabled) {
                    snowfall(
                        density = 0.005,
                    )
                } else {
                    this
                }
            }
            .testTag("HomeScreenContainer"),
    ) {
        HomeLoader(state)

        var tabFilter by rememberSaveable {
            mutableStateOf(TabFilter.COMMON)
        }

        TabsContent(
            tabFilter = tabFilter,
            items = state.items,
            isLoading = state is HomeLoadingState.Loading,
            navigateToSelector = navigateToSelector,
            navigateToContent = navigateToContent,
        )

        HomeTabs(
            onTabClick = {
                tabFilter = it
                onTabClick(it)
            },
        )
    }
}

@Composable
private fun HomeLoader(
    state: HomeLoadingState,
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
            visible = state is HomeLoadingState.Loading,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = pureStatusBarHeight(),
                        bottom = 12.dp,
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
    isLoading: Boolean,
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
            isLoading = isLoading,
            navigateToContent = navigateToSelectorInternal,
        )
        TabFilter.COUNTRY -> CountriesList(
            items = items,
            isLoading = isLoading,
            navigateToContent = navigateToSelectorInternal,
        )
        TabFilter.PLACE -> PlacesList(
            items = items,
            isLoading = isLoading,
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
            )
            .navigationBarsPadding()
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
    Box(
        modifier = Modifier
            .height(40.dp)
            .padding(4.dp),
    ) {
        val isSelected = selectedIndex.value == index
        Tab(
            selected = isSelected,
            onClick = {
                if (selectedIndex.value != index) {
                    selectedIndex.value = index
                    onTabClick(first)
                }
            },
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