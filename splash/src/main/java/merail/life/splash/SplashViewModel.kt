package merail.life.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import merail.life.auth.api.IFirebaseAuthRepository
import merail.life.core.RequestResult
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    firebaseAuthRepository: IFirebaseAuthRepository,
) : ViewModel() {
    val uiState: StateFlow<SplashUiState> = firebaseAuthRepository
        .authAnonymously()
        .map(RequestResult<Unit>::toState)
        .stateIn(viewModelScope, SharingStarted.Lazily, SplashUiState.Loading)
}
