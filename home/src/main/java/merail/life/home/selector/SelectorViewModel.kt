package merail.life.home.selector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import merail.life.core.mappers.RequestResult
import merail.life.data.api.IDataRepository
import merail.life.data.api.model.HomeElementModel
import merail.life.navigation.domain.NavigationRoute
import javax.inject.Inject

@HiltViewModel
internal class SelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dataRepository: IDataRepository,
) : ViewModel() {

    private val selectorFilter = savedStateHandle.toRoute<NavigationRoute.Selector>()

    val selectionState = dataRepository
        .getHomeElementsFromDatabase(
            selectorFilter = selectorFilter.selectorFilterType,
        )
        .map(RequestResult<List<HomeElementModel>>::toState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = SelectionState.None,
        )
}