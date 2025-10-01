package merail.life.home.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import merail.life.auth.api.IAuthRepository
import merail.life.home.main.useCases.LoadHomeElementsByTabUseCase
import merail.life.home.main.useCases.LoadHomeElementsUseCase
import merail.life.home.model.TabFilter
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    authRepository: IAuthRepository,
    private val loadHomeElementsUseCase: LoadHomeElementsUseCase,
    private val loadHomeElementsByTabUseCase: LoadHomeElementsByTabUseCase,
) : ViewModel() {

    companion object {
        internal const val TAG = "HomeViewModel"
    }

    private val _state = MutableStateFlow<HomeLoadingState>(HomeLoadingState.Loading())

    val state: StateFlow<HomeLoadingState> = _state

    val isSnowfallEnabled = authRepository.isSnowfallEnabled()

    init {
        viewModelScope.launch {
            loadHomeElementsUseCase().collect { state ->
                _state.update { state }
            }
        }
    }

    fun getHomeItems(
        filter: TabFilter,
    ) = viewModelScope.launch {
        loadHomeElementsByTabUseCase(filter).collect { state ->
            _state.update { state }
        }
    }
}
