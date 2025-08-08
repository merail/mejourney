package merail.life.home.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import merail.life.auth.api.IAuthRepository
import merail.life.core.mappers.RequestResult
import merail.life.data.api.IDataRepository
import merail.life.data.api.model.HomeElementModel
import merail.life.home.model.TabFilter
import merail.life.home.model.toModel
import javax.inject.Inject


@HiltViewModel
internal class HomeViewModel @Inject constructor(
    authRepository: IAuthRepository,
    private val dataRepository: IDataRepository,
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _state = MutableStateFlow<HomeLoadingState>(HomeLoadingState.None)

    val state: StateFlow<HomeLoadingState> = _state

    val isSnowfallEnabled = authRepository.isSnowfallEnabled()

    private var _isInit: Boolean = true

    init {
        viewModelScope.launch {
            dataRepository.getHomeElements().map(RequestResult<List<HomeElementModel>>::toState).collect {
                if (_isInit) {
                    when (it) {
                        is HomeLoadingState.Loading -> Log.d(TAG, "Получение списка элементов. Старт")
                        is HomeLoadingState.UnauthorizedException -> Log.w(TAG, "Получение списка элементов. Ошибка авторизации", it.exception)
                        is HomeLoadingState.CommonError -> Log.w(TAG, "Получение списка элементов. Ошибка", it.exception)
                        is HomeLoadingState.Success -> Log.d(TAG, "Получение списка элементов. Успех")
                        is HomeLoadingState.None,
                        -> Unit
                    }
                    _state.value = it
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
            _state.value = it
        }
    }
}
