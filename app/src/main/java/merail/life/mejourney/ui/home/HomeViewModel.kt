package merail.life.mejourney.ui.home

import androidx.lifecycle.ViewModel
import merail.life.mejourney.data.FirebaseStorageRepository
import merail.life.mejourney.data.HomeItem

class HomeViewModel(
    private val firebaseStorageRepository: FirebaseStorageRepository,
) : ViewModel()

data class HomeUiState(val itemList: List<HomeItem> = listOf())
