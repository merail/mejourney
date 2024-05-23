package merail.life.mejourney.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import merail.life.mejourney.data.HomeItem
import merail.life.mejourney.ui.home.common.Cover
import merail.life.mejourney.ui.home.common.ItemsParameterProvider
import merail.life.mejourney.ui.theme.MejourneyTheme

@Composable
fun ColumnScope.CountriesList(
    items: ImmutableList<HomeItem>,
    navigateToEvent: () -> Unit,
) {
    MejourneyTheme {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 4.dp,
                    top = 32.dp,
                    end = 4.dp,
                ),
        ) {
            items(items) {
                Card(
                    colors = CardDefaults.cardColors().copy(
                        containerColor = Color.Black,
                        contentColor = Color.White,
                    ),
                ) {
                    Box {
                        Cover(
                            item = it,
                            contentScale = ContentScale.FillWidth,
                            navigateToEvent = navigateToEvent,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(256.dp),
                        )

                        Text(
                            text = it.country,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .padding(16.dp),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CountriesListPreview(
    @PreviewParameter(ItemsParameterProvider::class) items: ImmutableList<HomeItem>,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        CountriesList(
            items = items,
            navigateToEvent = {},
        )
    }
}
