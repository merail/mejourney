package merail.life.mejourney.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import merail.life.firebase.data.IFirebaseRepository
import merail.life.firebase.data.model.HomeModel
import merail.life.firebase.data.model.HomeFilterType
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseRepository: IFirebaseRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.Loading)

    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        getItems(HomeFilterType.COMMON)
    }

    fun getItems(
        filter: HomeFilterType,
    ) = viewModelScope.launch {
        runCatching {
            firebaseRepository.getHomeItems(filter)
        }.onFailure {
            _uiState.value = HomeUiState.Error(it)
        }.onSuccess {
            _uiState.value = HomeUiState.Success(it.toImmutableList())
        }
    }
}

sealed class HomeUiState {

    data object Loading: HomeUiState()

    data class Error(val exception: Throwable): HomeUiState()

    data class Success(
        val items: ImmutableList<HomeModel> = persistentListOf(),
    ): HomeUiState()
}
