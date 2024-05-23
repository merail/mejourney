package merail.life.mejourney.ui.home

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.collections.immutable.ImmutableList
import merail.life.mejourney.R
import merail.life.mejourney.data.HomeItem
import merail.life.mejourney.data.TabFilter
import merail.life.mejourney.navigation.NavigationDestination
import merail.life.mejourney.ui.AppViewModelProvider
import merail.life.mejourney.ui.home.common.Error
import merail.life.mejourney.ui.home.common.ItemsParameterProvider
import merail.life.mejourney.ui.home.common.Loading
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
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is HomeUiState.Loading -> Loading()
        is HomeUiState.Error -> Error(uiState.exception.message.orEmpty())
        is HomeUiState.Success -> Content(
            items = uiState.items,
            navigateToEvent = navigateToEvent,
            onTabClick = {
                viewModel.getItems(it)
            },
        )
    }
}

@Preview
@Composable
private fun Content(
    @PreviewParameter(ItemsParameterProvider::class) items: ImmutableList<HomeItem>,
    navigateToEvent: () -> Unit = {},
    onTabClick: (TabFilter) -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        var tabFilter by remember {
            mutableStateOf(TabFilter.COMMON)
        }

        when (tabFilter) {
            TabFilter.YEAR -> YearsList(
                items = items,
                navigateToEvent = navigateToEvent,
            )
            TabFilter.COUNTRY -> CountriesList(
                items = items,
                navigateToEvent = navigateToEvent,
            )
            TabFilter.PLACE -> PlacesList(
                items = items,
                navigateToEvent = navigateToEvent,
            )
            TabFilter.COMMON -> CommonList(
                items = items,
                navigateToEvent = navigateToEvent,
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
        list.forEachIndexed { index, (filter, text) ->
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .padding(4.dp),
            ) {
                val isSelected = selectedIndex == index
                Tab(
                    selected = isSelected,
                    onClick = {
                        selectedIndex = index
                        onTabClick.invoke(filter)
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