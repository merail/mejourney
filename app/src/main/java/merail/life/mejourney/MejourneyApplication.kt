package merail.life.mejourney

import android.app.Application
import coil.ImageLoaderFactory
import com.parse.Parse
import dagger.hilt.android.HiltAndroidApp
import merail.life.design.extensions.createImageLoader

@HiltAndroidApp
internal class MejourneyApplication : Application(), ImageLoaderFactory {

    override fun newImageLoader() = createImageLoader()

    override fun onCreate() {
        super.onCreate()
            Parse.initialize(
                Parse.Configuration.Builder(this)
                    .applicationId(getString(R.string.back4app_app_id))
                    .clientKey(getString(R.string.back4app_client_key))
                    .server(getString(R.string.back4app_server_url))
                    .build(),
            )
    }
}