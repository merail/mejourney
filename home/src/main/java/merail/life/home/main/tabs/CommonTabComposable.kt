package merail.life.home.main.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import merail.life.design.MejourneyTheme
import merail.life.design.components.CoverImage
import merail.life.domain.TestTags
import merail.life.home.model.HomeItem

@Composable
internal fun ColumnScope.CommonList(
    items: ImmutableList<HomeItem>,
    navigateToContent: (String) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(
            bottom = 4.dp,
        ),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .weight(1f)
            .padding(4.dp)
            .testTag(TestTags.COMMON_LIST),
    ) {
        items(
            items = items,
            key = {
                it.id
            },
            contentType = {
                it
            },
        ) {
            CommonItem(
                item = it,
                navigateToContent = navigateToContent,
            )
        }
    }
}

@Composable
private fun CommonItem(
    item: HomeItem,
    navigateToContent: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .animateContentSize(),
    ) {
        var isImageLongClicked by remember {
            mutableStateOf(false)
        }

        CoverImage(
            id = item.id,
            url = item.url,
            navigateTo = navigateToContent,
            onLongClick = {
                isImageLongClicked = isImageLongClicked.not()
            },
        )

        AnimatedImageText(
            isVisible = isImageLongClicked,
            item = item,
        )
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
                style = MejourneyTheme.typography.titleLarge,
            )

            Text(
                text = item.description,
                style = MejourneyTheme.typography.bodyLarge,
            )
        }
    }
}