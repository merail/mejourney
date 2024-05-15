package merail.life.mejourney.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import merail.life.mejourney.data.HomeItem

@Composable
fun ColumnScope.CommonList(
    items: ImmutableList<HomeItem>,
    navigateToEvent: () -> Unit,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
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
