package merail.life.mejourney

import android.app.Application
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import merail.life.design.extensions.createImageLoader

@HiltAndroidApp
internal class MejourneyApplication : Application(), ImageLoaderFactory {
    override fun newImageLoader() = createImageLoader()
}