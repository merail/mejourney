package merail.life.home.main.tabs

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import merail.life.design.MejourneyTheme
import merail.life.design.cardColors
import merail.life.design.components.CoverImage
import merail.life.design.components.ImageLoading
import merail.life.home.R
import merail.life.home.model.HomeItem

@Composable
internal fun ColumnScope.YearsList(
    items: ImmutableList<HomeItem>,
    navigateToContent: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .padding(
                start = 4.dp,
                top = 24.dp,
                end = 4.dp,
                bottom = 4.dp,
            ),
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
            YearItem(
                item = it,
                navigateToContent = navigateToContent,
            )
        }
    }
}

@Composable
private fun YearItem(
    item: HomeItem,
    navigateToContent: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(
                vertical = 12.dp
            ),
    ) {
        var isImageLoaded by remember {
            mutableStateOf(false)
        }

        val onLoadingSuccess = remember {
            {
                isImageLoaded = true
            }
        }

        if (isImageLoaded) {
            Text(
                text = stringResource(
                    id = R.string.years_tab_element_title,
                    item.year,
                ),
                style = MejourneyTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                    ),
            )
        }

        Card(
            colors = MejourneyTheme.colors.cardColors,
            modifier = Modifier
                .padding(
                    top = 12.dp,
                ),
        ) {
            CoverImage(
                id = item.id,
                url = item.url,
                loading = {
                    ImageLoading(Modifier.height(224.dp))
                },
                onLoadingSuccess = onLoadingSuccess,
                navigateTo = navigateToContent,
                modifier = Modifier
                    .height(224.dp),
            )
        }
    }
}
