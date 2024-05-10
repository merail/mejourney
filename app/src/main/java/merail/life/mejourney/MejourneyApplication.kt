package merail.life.mejourney

import android.app.Application
import merail.life.mejourney.auth.AuthContainer
import merail.life.mejourney.auth.FirebaseAuthContainer
import merail.life.mejourney.data.DataContainer
import merail.life.mejourney.data.FirebaseDataContainer

class MejourneyApplication : Application() {
    lateinit var dataContainer: DataContainer

    lateinit var authContainer: AuthContainer

    override fun onCreate() {
        super.onCreate()

        dataContainer = FirebaseDataContainer()

        authContainer = FirebaseAuthContainer()
    }
}