package merail.life.home.main

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.idapgroup.snowfall.snowfall
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import merail.life.design.extensions.robustStatusBarHeight
import merail.life.home.R
import merail.life.home.model.HomeItem
import merail.life.home.model.SelectorFilter
import merail.life.home.model.TabFilter

internal val tabsList = persistentListOf(
    Pair(TabFilter.YEAR, R.string.main_tab_years_name),
    Pair(TabFilter.COUNTRY, R.string.main_tab_countries_name),
    Pair(TabFilter.PLACE, R.string.main_tab_places_name),
    Pair(TabFilter.COMMON, R.string.main_tab_all_name),
)

@Composable
internal fun rememberNavigateToContent(
    tabFilter: TabFilter,
    items: ImmutableList<HomeItem>,
    navigateToSelector: (SelectorFilter) -> Unit,
): (String) -> Unit = remember(items, tabFilter, navigateToSelector) {
    { id ->
        items.find { item ->
            item.id == id
        }?.run {
            navigateToSelector(
                when (tabFilter) {
                    TabFilter.YEAR -> SelectorFilter.Year(year)
                    TabFilter.COUNTRY -> SelectorFilter.Country(country)
                    else -> SelectorFilter.Place(place)
                }
            )
        }
    }
}

@Composable
internal fun rememberStableLoading(
    isLoading: Boolean,
    minDurationMs: Long = 1_000,
): Boolean {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            visible = true
        } else {
            delay(minDurationMs)
            visible = false
        }
    }

    return visible
}

@Composable
internal fun rememberTopPaddingAnimation(
    isLoading: Boolean,
    label: String = "TopPaddingAnimation",
): Dp {
    val topPadding by animateDpAsState(
        targetValue = if (isLoading) {
            0.dp
        } else {
            robustStatusBarHeight()
        },
        label = label,
    )
    return topPadding
}

@Composable
internal fun Modifier.snowfall(
    isSnowfallEnabled: Boolean,
) = if (isSnowfallEnabled) {
    snowfall(
        density = 0.005,
    )
} else {
    this
}