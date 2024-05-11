package merail.life.mejourney.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import kotlinx.collections.immutable.ImmutableList
import merail.life.mejourney.R
import merail.life.mejourney.data.HomeItem
import merail.life.mejourney.navigation.NavigationDestination
import merail.life.mejourney.ui.AppViewModelProvider

object HomeDestination : NavigationDestination {
    override val route = "home"
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is HomeUiState.Loading -> Loading()
        is HomeUiState.Error -> Error(uiState.exception.message.orEmpty())
        is HomeUiState.Success -> MainList(uiState.items)
    }
}

@Composable
private fun Loading() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Error(
    errorMessage: String,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Text(
            text = errorMessage,
            color = Color.Red,
        )
    }
}

@Composable
private fun MainList(
    items: ImmutableList<HomeItem>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        items(items) {
            Cover(it)
        }
    }
}

@Composable
private fun Cover(
    item: HomeItem,
) {
    Box(
        modifier = Modifier
            .padding(12.dp),
    ) {
        val isImageLoaded = remember {
            mutableStateOf(false)
        }

        SubcomposeAsyncImage(
            model = item.url,
            contentDescription = null,
            loading = {
                ImageLoading()
            },
            onSuccess = {
                isImageLoaded.value = true
            },
        )

        if (isImageLoaded.value) {
            Text(
                text = item.title.uppercase(),
                color = Color.White,
                fontSize = 48.sp,
                fontFamily = FontFamily(Font(R.font.open_sans_bold)),
                modifier = Modifier
                    .padding(12.dp),
            )
        }
    }
}

@Composable
private fun ImageLoading() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(64.dp),
    ) {
        CircularProgressIndicator()
    }
}
