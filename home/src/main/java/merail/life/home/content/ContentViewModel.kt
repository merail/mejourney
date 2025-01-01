package merail.life.home.content

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import merail.life.core.RequestResult
import merail.life.data.IDataRepository
import merail.life.data.model.ContentModel
import merail.life.navigation.domain.NavigationRoute
import javax.inject.Inject

@HiltViewModel
internal class ContentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dataRepository: IDataRepository,
) : ViewModel() {

    private val contentId = checkNotNull(savedStateHandle.toRoute<NavigationRoute.Content>().contentId)

    var contentLoadingState = dataRepository
        .getContent(contentId)
        .map(RequestResult<ContentModel>::toState)
        .stateIn(viewModelScope, SharingStarted.Lazily, ContentLoadingState.Loading)
        private set
}
