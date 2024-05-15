package merail.life.mejourney.ui.home

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import merail.life.mejourney.data.HomeItem

@Composable
fun ColumnScope.PlacesList(
    items: ImmutableList<HomeItem>,
    navigateToEvent: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .weight(1f)
            .padding(4.dp),
    ) {
        items(items) {
            Cover(
                item = it,
                navigateToEvent = navigateToEvent,
                modifier = Modifier
                    .padding(
                        vertical = 2.dp,
                    )
            )
        }
    }
}
