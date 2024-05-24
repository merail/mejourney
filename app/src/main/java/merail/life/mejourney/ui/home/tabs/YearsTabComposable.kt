package merail.life.mejourney.ui.home.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import merail.life.mejourney.R
import merail.life.mejourney.data.model.HomeItem
import merail.life.mejourney.ui.common.Cover
import merail.life.mejourney.ui.common.ItemsParameterProvider
import merail.life.mejourney.ui.theme.MejourneyTheme

@Composable
fun ColumnScope.YearsList(
    items: ImmutableList<HomeItem>,
    navigateToContent: (String) -> Unit,
) {
    MejourneyTheme {
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
            items(items) {
                YearItem(
                    item = it,
                    navigateToContent = navigateToContent,
                )
            }
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
        val isImageLoaded = if (LocalInspectionMode.current) {
            remember {
                mutableStateOf(true)
            }
        } else {
            remember {
                mutableStateOf(false)
            }
        }

        if (isImageLoaded.value) {
            Text(
                text = stringResource(
                    id = R.string.years_tab_element_title,
                    item.year,
                ),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                    ),
            )
        }

        Card(
            colors = CardDefaults.cardColors().copy(
                containerColor = Color.Black,
            ),
            modifier = Modifier
                .padding(
                    top = 12.dp,
                ),
        ) {
            Cover(
                item = item,
                onLoadingSuccess = {
                    isImageLoaded.value = true
                },
                navigateToContent = navigateToContent,
            )
        }
    }
}

@Preview
@Composable
private fun YearsListPreview(
    @PreviewParameter(ItemsParameterProvider::class) items: ImmutableList<HomeItem>,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        YearsList(
            items = items,
            navigateToContent = {},
        )
    }
}
