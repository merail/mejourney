package merail.life.home.content

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import merail.life.core.RequestResult
import merail.life.data.IDataRepository
import merail.life.data.model.ContentModel
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dataRepository: IDataRepository,
) : ViewModel() {

    private val contentId: String = checkNotNull(savedStateHandle[ContentDestination.CONTENT_ID_ARG])

    val uiState: StateFlow<ContentUiState> = dataRepository
        .getContent(contentId)
        .map(RequestResult<ContentModel>::toState)
        .stateIn(viewModelScope, SharingStarted.Lazily, ContentUiState.Loading)
}
