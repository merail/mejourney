package merail.life.home.content

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import merail.life.firebase.data.IFirebaseRepository
import merail.life.home.model.ContentItem
import merail.life.home.model.toItem
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val firebaseRepository: IFirebaseRepository,
) : ViewModel() {

    private val contentId: String = checkNotNull(savedStateHandle[ContentDestination.CONTENT_ID_ARG])

    private val _uiState: MutableStateFlow<ContentUiState> = MutableStateFlow(ContentUiState.Loading)

    val uiState: StateFlow<ContentUiState> = _uiState

    init {
        getItem(contentId)
    }

    private fun getItem(
        id: String,
    ) = viewModelScope.launch {
        runCatching {
            firebaseRepository.getContentItem(id)
        }.onFailure {
            _uiState.value = ContentUiState.Error(it)
        }.onSuccess {
            _uiState.value = ContentUiState.Success(it.toItem())
        }
    }
}

sealed class ContentUiState {

    data object Loading: ContentUiState()

    data class Error(val exception: Throwable): ContentUiState()

    class Success(
        val item: ContentItem,
    ): ContentUiState()
}
