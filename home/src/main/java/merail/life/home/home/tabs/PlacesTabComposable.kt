package merail.life.home.home.tabs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import merail.life.design.MejourneyTheme
import merail.life.design.components.CoverImage
import merail.life.home.model.HomeItem

@Composable
fun ColumnScope.PlacesList(
    items: ImmutableList<HomeItem>,
    navigateToContent: (String) -> Unit,
) {
    MejourneyTheme {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
        ) {
            items(items) {
                PlaceItem(
                    item = it,
                    navigateToContent = navigateToContent,
                )
            }
        }
    }
}

@Composable
private fun PlaceItem(
    item: HomeItem,
    navigateToContent: (String) -> Unit,
) {
    Column {
        val isImageLoaded = if (LocalInspectionMode.current) {
            remember {
                mutableStateOf(true)
            }
        } else {
            remember {
                mutableStateOf(false)
            }
        }

        Card(
            colors = CardDefaults.cardColors().copy(
                containerColor = Color.Black,
            ),
            border = BorderStroke(1.dp, Color.White),
            modifier = Modifier
                .padding(
                    top = 12.dp,
                ),
        ) {
            CoverImage(
                id = item.id,
                url = item.url,
                onLoadingSuccess = {
                    isImageLoaded.value = true
                },
                navigateTo = navigateToContent,
            )
        }

        if (isImageLoaded.value) {
            Text(
                text = item.place,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(
                        vertical = 12.dp,
                    ),
            )
        }
    }
}
