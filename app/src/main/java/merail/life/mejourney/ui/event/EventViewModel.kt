package merail.life.mejourney.ui.event

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EventViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<EventUiState> = MutableStateFlow(EventUiState.Success)

    val uiState: StateFlow<EventUiState> = _uiState
}

sealed class EventUiState {

    data object Loading: EventUiState()

    data class Error(val exception: Throwable): EventUiState()

    data object Success: EventUiState()
}
