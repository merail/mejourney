package merail.life.mejourney.ui.home.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import merail.life.firebase.data.model.HomeModel
import merail.life.mejourney.ui.common.Cover
import merail.life.mejourney.ui.common.ItemsParameterProvider
import merail.life.mejourney.ui.theme.MejourneyTheme

@Composable
fun ColumnScope.CommonList(
    items: ImmutableList<HomeModel>,
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

                    Cover(
                        item = it,
                        navigateToContent = navigateToContent,
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
    item: HomeModel,
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

@Preview
@Composable
private fun CommonListPreview(
    @PreviewParameter(ItemsParameterProvider::class) items: ImmutableList<HomeModel>,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        CommonList(
            items = items,
            navigateToContent = {},
        )
    }
}