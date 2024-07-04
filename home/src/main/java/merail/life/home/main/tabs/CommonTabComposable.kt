package merail.life.home.main.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import merail.life.design.MejourneyTheme
import merail.life.design.components.CoverImage
import merail.life.home.model.HomeItem

@Composable
internal fun ColumnScope.CommonList(
    items: ImmutableList<HomeItem>,
    navigateToContent: (String) -> Unit,
) {
    MejourneyTheme {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
        ) {
            items(items) {
                Column(
                    modifier = Modifier
                        .animateContentSize(),
                ) {
                    val isImageLongClicked = remember {
                        mutableStateOf(false)
                    }

                    CoverImage(
                        id = it.id,
                        url = it.url,
                        navigateTo = navigateToContent,
                        onLongClick = {
                            isImageLongClicked.value = isImageLongClicked.value.not()
                        },
                    )

                    AnimatedImageText(
                        isVisible = isImageLongClicked.value,
                        item = it,
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedImageText(
    isVisible: Boolean,
    item: HomeItem,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically(),
        exit = shrinkVertically(),
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleLarge,
            )

            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}