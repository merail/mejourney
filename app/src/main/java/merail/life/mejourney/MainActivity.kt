package merail.life.mejourney

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import merail.life.core.errors.toType
import merail.life.core.navigation.NavigationRoute
import merail.life.core.permissions.NotificationsPermissionRequester
import merail.life.design.MejourneyTheme
import merail.life.mejourney.activity.MainAuthState
import merail.life.mejourney.activity.MainViewModel
import merail.life.mejourney.navigation.MejourneyNavHost
import merail.life.mejourney.navigation.getRouteIfExists
import merail.tools.permissions.runtime.runtimePermissionRequester


@AndroidEntryPoint
internal class MainActivity : ComponentActivity(), NotificationsPermissionRequester {

    private val viewModel by viewModels<MainViewModel>()

    @delegate:RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val runtimePermissionRequester by runtimePermissionRequester(
        requestedPermission = Manifest.permission.POST_NOTIFICATIONS,
    )

    private var intentRoute: MutableState<NavigationRoute?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )

        installSplashScreen().setKeepOnScreenCondition {
            viewModel.state.value is MainAuthState.Loading
        }

        super.onCreate(savedInstanceState)

        actionBar?.hide()

        setContent {
            MejourneyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    intentRoute = remember {
                        mutableStateOf(intent.getRouteIfExists())
                    }

                    val state by viewModel.state.collectAsState()

                    MejourneyNavHost(
                        intentRoute = intentRoute,
                        errorType = (state as? MainAuthState.Error)?.exception?.toType(),
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intentRoute?.value = intent.getRouteIfExists()
    }

    override fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (runtimePermissionRequester.areAllPermissionsGranted().not()) {
                runtimePermissionRequester.requestPermissions()
            }
        }
    }
}