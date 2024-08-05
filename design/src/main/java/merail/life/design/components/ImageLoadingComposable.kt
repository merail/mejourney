package merail.life.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.valentinilk.shimmer.shimmer
import merail.life.design.MejourneyTheme
import merail.life.design.shimmerColor

@Composable
fun ImageLoading(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .shimmer(),
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(MejourneyTheme.colors.shimmerColor),
        )
    }
}