package merail.life.home.main

import androidx.activity.compose.LocalActivity
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
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
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import merail.life.core.permissions.NotificationsPermissionRequester
import merail.life.data.api.model.SelectorFilterType
import merail.life.design.MejourneyTheme
import merail.life.design.components.Loading
import merail.life.design.extensions.robustNavigationBarHeight
import merail.life.design.extensions.robustStatusBarHeight
import merail.life.design.selectedTabColor
import merail.life.design.tabsContainerColor
import merail.life.design.unselectedTabColor
import merail.life.design.unselectedTabTextColor
import merail.life.domain.TestTags
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

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        when (val currentState = state) {
            is HomeLoadingState.Error -> onError(currentState.exception)
            is HomeLoadingState.Success -> {
                (activity as? NotificationsPermissionRequester)?.requestPermission()
            }
            is HomeLoadingState.Loading,
            -> Unit
        }
    }

    val isSnowfallEnabled by viewModel.isSnowfallEnabledState.collectAsStateWithLifecycle()

    HomeContent(
        state = state,
        isSnowfallEnabled = isSnowfallEnabled,
        navigateToSelector = {
            navigateToSelector(it.toModel())
        },
        navigateToContent = navigateToContent,
        onTabClick = viewModel::getHomeItems,
    )
}

@VisibleForTesting
@Composable
internal fun HomeContent(
    state: HomeLoadingState,
    isSnowfallEnabled: Boolean,
    navigateToSelector: (SelectorFilter) -> Unit,
    navigateToContent: (String) -> Unit = {},
    onTabClick: (TabFilter) -> Unit = {},
) {
    var tabFilter by rememberSaveable {
        mutableStateOf(TabFilter.COMMON)
    }

    val isLoading = rememberStableLoading(
        isLoading = state is HomeLoadingState.Loading,
    )

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .snowfall(isSnowfallEnabled)
            .fillMaxSize()
            .testTag(TestTags.HOME_SCREEN_CONTAINER),
    ) {
        HomeLoader(
            isLoading = isLoading,
            isGlobalLoading = state.isGlobalLoading,
        )

        TabsContent(
            tabFilter = tabFilter,
            items = state.items,
            isLoading = isLoading,
            navigateToSelector = navigateToSelector,
            navigateToContent = navigateToContent,
        )

        HomeTabs(
            onTabClick = { selectedTab ->
                tabFilter = selectedTab
                onTabClick(selectedTab)
            },
        )
    }
}

@Composable
private fun HomeLoader(
    isLoading: Boolean,
    isGlobalLoading: Boolean,
) {
    if (isGlobalLoading) {
        Loading()
    } else {
        AnimatedVisibility(
            visible = isLoading,
            enter = expandVertically(),
            exit = shrinkVertically(),
            modifier = Modifier
                .testTag(TestTags.TOP_LOADER),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = robustStatusBarHeight(),
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
    val navigateToSelectorInternal = rememberNavigateToContent(
        tabFilter = tabFilter,
        items = items,
        navigateToSelector = navigateToSelector,
    )

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

@Composable
private fun HomeTabs(
    onTabClick: (TabFilter) -> Unit,
) {
    var selectedIndex by rememberSaveable {
        mutableIntStateOf(tabsList.size - 1)
    }

    SecondaryTabRow(
        selectedTabIndex = selectedIndex,
        containerColor = MejourneyTheme.colors.tabsContainerColor,
        indicator = {},
        divider = {},
        modifier = Modifier
            .padding(
                start = 24.dp,
                top = 8.dp,
                end = 24.dp,
                bottom = robustNavigationBarHeight(),
            )
            .clip(RoundedCornerShape(64)),
    ) {
        tabsList.forEachIndexed { index, tabElement ->
            val isSelected = selectedIndex == index

            HomeTab(
                tabFilter = tabElement.first,
                textRes = tabElement.second,
                isSelected = isSelected,
                index = index,
                onTabClick = { index, filter ->
                    selectedIndex = index
                    onTabClick(filter)
                },
            )
        }
    }
}

@Composable
private fun HomeTab(
    tabFilter: TabFilter,
    @StringRes textRes: Int,
    isSelected: Boolean,
    index: Int,
    onTabClick: (Int, TabFilter) -> Unit,
) {
    Box(
        modifier = Modifier
            .height(40.dp)
            .padding(4.dp),
    ) {
        Tab(
            selected = isSelected,
            onClick = {
                if (isSelected.not()) {
                    onTabClick(index, tabFilter)
                }
            },
            text = {
                Text(
                    text = stringResource(textRes),
                    color = if (isSelected) {
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
                    color = if (isSelected) {
                        MejourneyTheme.colors.selectedTabColor
                    } else {
                        MejourneyTheme.colors.unselectedTabColor
                    },
                )
                .testTag("${TestTags.HOME_TAB}_${tabFilter.ordinal}"),
        )
    }
}