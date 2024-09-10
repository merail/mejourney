package merail.life.auth.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import merail.life.auth.api.IAuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<AuthUiState> = MutableStateFlow(AuthUiState.None)

    val uiState: StateFlow<AuthUiState> = _uiState

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    fun updateEmail(
        email: String,
    ) {
        this.email = email
    }

    fun updatePassword(
        password: String,
    ) {
        this.password = password
    }

    fun createUser() {
        viewModelScope.launch {
            authRepository.createUser(
                email = email,
                password = password,
            ).collect {

            }
        }
    }
}

sealed class AuthUiState {

    data object None : AuthUiState()

    data class Error(val exception: Throwable): AuthUiState()

    data object Success: AuthUiState()
}
