package merail.life.home.main.tabs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import merail.life.design.MejourneyTheme
import merail.life.design.cardColors
import merail.life.design.components.CoverImage
import merail.life.home.model.HomeItem

@Composable
internal fun ColumnScope.PlacesList(
    items: ImmutableList<HomeItem>,
    navigateToContent: (String) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
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
            PlaceItem(
                item = it,
                navigateToContent = navigateToContent,
            )
        }
    }
}

@Composable
private fun PlaceItem(
    item: HomeItem,
    navigateToContent: (String) -> Unit,
) {
    Column {
        val isImageLoaded = remember {
            mutableStateOf(false)
        }

        val onLoadingSuccess = remember {
            {
                isImageLoaded.value = true
            }
        }

        Card(
            colors = MejourneyTheme.colors.cardColors,
            border = BorderStroke(1.dp, MejourneyTheme.colors.borderPrimary),
            modifier = Modifier
                .padding(
                    top = 12.dp,
                ),
        ) {
            CoverImage(
                id = item.id,
                url = item.url,
                onLoadingSuccess = onLoadingSuccess,
                navigateTo = navigateToContent,
                modifier = Modifier
                    .height(512.dp),
            )
        }

        if (isImageLoaded.value) {
            Text(
                text = item.place,
                style = MejourneyTheme.typography.titleLarge,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(
                        vertical = 12.dp,
                    ),
            )
        }
    }
}
