package merail.life.mejourney.ui.home

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import merail.life.mejourney.data.HomeItem

@Composable
fun ColumnScope.CountriesList(
    items: ImmutableList<HomeItem>,
    navigateToEvent: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .padding(4.dp),
    ) {
        items(items) {
            Card(
                colors = CardDefaults.cardColors().copy(
                    containerColor = Color.Black,
                ),
                modifier = Modifier
                    .padding(12.dp),
            ) {
                Cover(
                    item = it,
                    navigateToEvent = navigateToEvent,
                )
            }
        }
    }
}
