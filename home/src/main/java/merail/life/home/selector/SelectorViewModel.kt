package merail.life.home.selector

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
import merail.life.data.model.HomeElementModel
import merail.life.data.model.SelectorFilterType
import javax.inject.Inject

@HiltViewModel
class SelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dataRepository: IDataRepository,
) : ViewModel() {

    private val selectorFilter: SelectorFilterType = checkNotNull(
        value = savedStateHandle[SelectorDestination.SELECTOR_FILTER_ARG],
    )

    val uiState: StateFlow<SelectorUiState> = dataRepository
        .getHomeElementsFromDatabase(
            selectorFilter = selectorFilter,
        )
        .map(RequestResult<List<HomeElementModel>>::toState)
        .stateIn(viewModelScope, SharingStarted.Lazily, SelectorUiState.None)
}