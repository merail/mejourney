package merail.life.mejourney.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import merail.life.mejourney.data.HomeItem
import merail.life.mejourney.ui.home.common.Cover
import merail.life.mejourney.ui.home.common.ItemsParameterProvider
import merail.life.mejourney.ui.theme.MejourneyTheme

@Composable
fun ColumnScope.PlacesList(
    items: ImmutableList<HomeItem>,
    navigateToEvent: () -> Unit,
) {
    MejourneyTheme {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
        ) {
            items(items) {
                Cover(
                    item = it,
                    navigateToEvent = navigateToEvent,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PlacesListPreview(
    @PreviewParameter(ItemsParameterProvider::class) items: ImmutableList<HomeItem>,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        PlacesList(
            items = items,
            navigateToEvent = {},
        )
    }
}
