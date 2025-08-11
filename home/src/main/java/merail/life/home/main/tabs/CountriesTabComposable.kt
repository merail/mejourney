package merail.life.home.main.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import merail.life.design.MejourneyTheme
import merail.life.design.cardColors
import merail.life.design.components.CoverImage
import merail.life.design.extensions.pureStatusBarHeight
import merail.life.home.model.HomeItem

@Composable
internal fun ColumnScope.CountriesList(
    items: ImmutableList<HomeItem>,
    isLoading: Boolean,
    navigateToContent: (String) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier
            .padding(
                start = 4.dp,
                top = if (isLoading) {
                    0.dp
                } else {
                    pureStatusBarHeight()
                },
                end = 4.dp,
                bottom = 4.dp,
            )
            .weight(1f),
    ) {
        items(
            items = items,
            key = {
                it.id
            },
            contentType = {
                it
            },
        ) {
            CountryItem(
                item = it,
                navigateToContent = navigateToContent,
            )
        }
    }
}

@Composable
private fun CountryItem(
    item: HomeItem,
    navigateToContent: (String) -> Unit,
) {
    Card(
        colors = MejourneyTheme.colors.cardColors,
    ) {
        Box {
            var isImageLoaded by remember {
                mutableStateOf(false)
            }

            val onLoadingSuccess = remember {
                {
                    isImageLoaded = true
                }
            }

            CoverImage(
                id = item.id,
                url = item.url,
                onLoadingSuccess = onLoadingSuccess,
                navigateTo = navigateToContent,
                modifier = Modifier
                    .height(256.dp),
            )

            if (isImageLoaded) {
                Text(
                    text = item.country,
                    style = MejourneyTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(16.dp),
                )
            }
        }
    }
}
