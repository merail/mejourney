package merail.life.mejourney.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import merail.life.mejourney.MejourneyApplication
import merail.life.mejourney.ui.event.EventViewModel
import merail.life.mejourney.ui.home.HomeViewModel
import merail.life.mejourney.ui.splash.SplashViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            SplashViewModel(
                firebaseAuthRepository = mejourneyApplication()
                    .authContainer
                    .firebaseAuthRepository,
                firebaseStorageRepository = mejourneyApplication()
                    .dataContainer
                    .firebaseRepository,
            )
        }
        initializer {
            HomeViewModel(
                firebaseStorageRepository = mejourneyApplication()
                    .dataContainer
                    .firebaseRepository,
            )
        }
        initializer {
            EventViewModel()
        }
    }
}

fun CreationExtras.mejourneyApplication(): MejourneyApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as MejourneyApplication)
