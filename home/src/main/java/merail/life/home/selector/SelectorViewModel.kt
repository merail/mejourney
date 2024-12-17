package merail.life.home.selector

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
import merail.life.data.model.HomeElementModel
import merail.life.navigation.domain.NavigationRoute
import javax.inject.Inject

@HiltViewModel
class SelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dataRepository: IDataRepository,
) : ViewModel() {

    private val selectorFilter = savedStateHandle.toRoute<NavigationRoute.Selector>()

    var selectionState = dataRepository
        .getHomeElementsFromDatabase(
            selectorFilter = selectorFilter.selectorFilterType,
        )
        .map(RequestResult<List<HomeElementModel>>::toState)
        .stateIn(viewModelScope, SharingStarted.Lazily, SelectionState.None)
        private set
}