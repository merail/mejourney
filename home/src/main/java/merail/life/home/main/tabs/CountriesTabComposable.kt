package merail.life.home.main.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import merail.life.design.MejourneyTheme
import merail.life.design.components.CoverImage
import merail.life.home.model.HomeItem

@Composable
fun ColumnScope.CountriesList(
    items: ImmutableList<HomeItem>,
    navigateToContent: (String) -> Unit,
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
                CountryItem(
                    item = it,
                    navigateToContent = navigateToContent,
                )
            }
        }
    }
}

@Composable
private fun CountryItem(
    item: HomeItem,
    navigateToContent: (String) -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors().copy(
            containerColor = Color.Black,
            contentColor = Color.White,
        ),
    ) {
        Box {
            val isImageLoaded = if (LocalInspectionMode.current) {
                remember {
                    mutableStateOf(true)
                }
            } else {
                remember {
                    mutableStateOf(false)
                }
            }


            CoverImage(
                id = item.id,
                url = item.url,
                onLoadingSuccess = {
                    isImageLoaded.value = true
                },
                navigateTo = navigateToContent,
                modifier = Modifier
                    .height(256.dp),
            )

            if (isImageLoaded.value) {
                Text(
                    text = item.country,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(16.dp),
                )
            }
        }
    }
}
