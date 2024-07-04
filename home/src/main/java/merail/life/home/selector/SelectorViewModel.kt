package merail.life.home.selector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import merail.life.data.IDataRepository
import merail.life.data.data.model.HomeElementModel
import merail.life.data.data.model.HomeFilterType
import merail.life.data.data.model.SelectorFilterModel
import javax.inject.Inject
import merail.life.data.RequestResult

@HiltViewModel
class SelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dataRepository: IDataRepository,
) : ViewModel() {

    private val tabFilter: HomeFilterType =
        checkNotNull(savedStateHandle[SelectorDestination.TAB_FILTER_ARG])

    private val selectorFilter: SelectorFilterModel =
        SelectorFilterModel.Country("Турция")//checkNotNull(savedStateHandle[SelectorDestination.SELECTOR_FILTER_ARG])

    private val _uiState: MutableStateFlow<SelectorUiState> =
        MutableStateFlow(SelectorUiState.None)

    val uiState: StateFlow<SelectorUiState> = _uiState

    init {
        getItems(
            selectorFilter = selectorFilter,
        )
    }

    private fun getItems(
        selectorFilter: SelectorFilterModel,
    ) = viewModelScope.launch {
        dataRepository
            .getHomeElementsFromDatabase(
                selectorFilter = selectorFilter,
            )
            .map(RequestResult<List<HomeElementModel>>::toState)
            .collect {
                _uiState.value = it
            }
    }
}