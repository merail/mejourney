package merail.life.mejourney

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import merail.life.mejourney.state.MainAuthState
import kotlin.getValue

@AndroidEntryPoint
class HiltTestActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen().setKeepOnScreenCondition {
            viewModel.state.value is MainAuthState.Loading
        }

        super.onCreate(savedInstanceState)

        actionBar?.hide()
    }
}