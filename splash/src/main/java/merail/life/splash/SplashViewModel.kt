package merail.life.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import merail.life.auth.api.IAuthRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    authRepository: IAuthRepository,
) : ViewModel() {
    val isRegistered = authRepository.checkUser()
}
