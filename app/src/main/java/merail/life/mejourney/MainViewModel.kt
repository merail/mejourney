package merail.life.mejourney

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import merail.life.mejourney.state.MainAuthState
import merail.life.mejourney.useCases.AuthUseCase
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    companion object {
        internal const val TAG = "MainViewModel"
    }

    private val _state = MutableStateFlow<MainAuthState>(MainAuthState.Loading)

    val state: StateFlow<MainAuthState> = _state

    init {
        viewModelScope.launch {
            _state.update {
                authUseCase()
            }
        }
    }
}
