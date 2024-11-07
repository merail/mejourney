package merail.life.mejourney

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import merail.life.core.INotificationsPermissionRequester
import merail.life.design.MejourneyTheme
import merail.life.navigation.domain.NavigationRoute
import merail.life.navigation.domain.getRouteIfExists
import merail.tools.permissions.runtime.runtimePermissionRequester


@AndroidEntryPoint
internal class MainActivity : ComponentActivity(), INotificationsPermissionRequester {

    @delegate:RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val runtimePermissionRequester by runtimePermissionRequester(
        requestedPermission = Manifest.permission.POST_NOTIFICATIONS,
    )

    private lateinit var intentRoute: MutableState<NavigationRoute?>

    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        setContent {
            MejourneyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    intentRoute = remember {
                        mutableStateOf(intent.getRouteIfExists())
                    }

                    MejourneyApp(
                        intentRoute = intentRoute,
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intentRoute.value = intent.getRouteIfExists()
    }

    override fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (runtimePermissionRequester.areAllPermissionsGranted().not()) {
                runtimePermissionRequester.requestPermissions()
            }
        }
    }
}