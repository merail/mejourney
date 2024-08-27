package merail.life.mejourney

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import merail.life.design.MejourneyTheme
import merail.life.mejourney.navigation.getRouteIfExists
import merail.tools.permissions.runtime.RuntimePermissionRequester


@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {

    private lateinit var intentRoute: MutableState<String?>

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

        checkNotificationsPermission()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intentRoute.value = intent.getRouteIfExists()
    }

    private fun checkNotificationsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val runtimePermissionRequester = RuntimePermissionRequester(
                activity = this,
                requestedPermission = Manifest.permission.POST_NOTIFICATIONS,
            )
            if (runtimePermissionRequester.areAllPermissionsGranted().not()) {
                runtimePermissionRequester.requestPermissions()
            }
        }
    }
}