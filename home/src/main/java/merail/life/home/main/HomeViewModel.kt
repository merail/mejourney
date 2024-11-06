package merail.life.home.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import merail.life.core.RequestResult
import merail.life.data.IDataRepository
import merail.life.data.model.HomeElementModel
import merail.life.home.model.TabFilter
import merail.life.home.model.toModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataRepository: IDataRepository,
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.None)

    val uiState: StateFlow<HomeUiState> = _uiState

    private var _isInit: Boolean = true

    init {
        viewModelScope.launch {
            dataRepository.getHomeElements().map(RequestResult<List<HomeElementModel>>::toState).collect {
                if (_isInit) {
                    when (it) {
                        is HomeUiState.Loading -> Log.d(TAG, "Получение списка элементов. Старт")
                        is HomeUiState.UnauthorizedException -> Log.w(TAG, "Получение списка элементов. Ошибка авторизации", it.exception)
                        is HomeUiState.CommonError -> Log.w(TAG, "Получение списка элементов. Ошибка", it.exception)
                        is HomeUiState.Success -> Log.d(TAG, "Получение списка элементов. Успех")
                        is HomeUiState.None,
                        -> Unit
                    }
                    _uiState.value = it
                }
            }
        }
    }

    fun getHomeItems(
        filter: TabFilter,
    ) = viewModelScope.launch {
        _isInit = false
        dataRepository.getHomeElementsFromDatabase(
            tabFilter = filter.toModel(),
        ).map(RequestResult<List<HomeElementModel>>::toState).collect {
            _uiState.value = it
        }
    }
}
