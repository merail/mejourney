package merail.life.mejourney.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import merail.life.mejourney.MejourneyApplication
import merail.life.mejourney.ui.home.HomeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                firebaseStorageRepository = mejourneyApplication()
                    .container
                    .firebaseStorageRepository,
            )
        }
    }
}

fun CreationExtras.mejourneyApplication(): MejourneyApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as MejourneyApplication)
