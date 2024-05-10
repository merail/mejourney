package merail.life.mejourney

import android.app.Application
import merail.life.mejourney.data.AppContainer
import merail.life.mejourney.data.AppDataContainer

class MejourneyApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer()
    }
}