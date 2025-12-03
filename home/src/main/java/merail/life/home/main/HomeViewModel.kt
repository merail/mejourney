package merail.life.home.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import merail.life.auth.api.IAuthRepository
import merail.life.home.main.useCases.LoadHomeElementsByTabUseCase
import merail.life.home.main.useCases.LoadHomeElementsUseCase
import merail.life.home.main.useCases.LoadSnowfallStateUseCase
import merail.life.home.model.TabFilter
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    authRepository: IAuthRepository,
    private val loadHomeElementsUseCase: LoadHomeElementsUseCase,
    private val loadSnowfallStateUseCase: LoadSnowfallStateUseCase,
    private val loadHomeElementsByTabUseCase: LoadHomeElementsByTabUseCase,
) : ViewModel() {

    companion object {
        internal const val TAG = "HomeViewModel"
    }

    private val _state = MutableStateFlow<HomeLoadingState>(HomeLoadingState.Loading())

    val state: StateFlow<HomeLoadingState> = _state

    private val _isSnowfallEnabledState = MutableStateFlow(false)

    val isSnowfallEnabledState: StateFlow<Boolean> = _isSnowfallEnabledState

    init {
        viewModelScope.launch {
            authRepository.isAuthorized().filter {
                it
            }.collect {
                loadHomeElementsUseCase().collect {
                    _state.value = it
                }
            }
        }

        viewModelScope.launch {
            _isSnowfallEnabledState.value = loadSnowfallStateUseCase()
        }
    }

    fun getHomeItems(
        filter: TabFilter,
    ) = viewModelScope.launch {
        loadHomeElementsByTabUseCase(filter).collect {
            _state.value = it
        }
    }
}
