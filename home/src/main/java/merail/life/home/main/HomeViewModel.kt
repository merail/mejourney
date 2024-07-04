package merail.life.home.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import merail.life.core.RequestResult
import merail.life.data.IDataRepository
import merail.life.data.data.model.HomeElementModel
import merail.life.home.model.TabFilter
import merail.life.home.model.toModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataRepository: IDataRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.None)

    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        viewModelScope.launch {
            dataRepository
                .getHomeElements()
                .map(RequestResult<List<HomeElementModel>>::toState)
                .collect {
                    _uiState.value = it
                }
        }
    }

    fun getHomeItems(
        filter: TabFilter,
    ) = viewModelScope.launch {
        dataRepository
            .getHomeElementsFromDatabase(
                tabFilter = filter.toModel(),
            )
            .map(RequestResult<List<HomeElementModel>>::toState)
            .collect {
                _uiState.value = it
            }
    }
}
