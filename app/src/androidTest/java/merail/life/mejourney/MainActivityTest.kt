package merail.life.mejourney

import android.os.Build
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    companion object {
        private const val ENGLISH_ALLOW_WORD = "ALLOW"

        private const val RUSSIAN_ALLOW_WORD = "РАЗРЕШИТЬ"

        private const val WAITING_TIME = 5_000L
    }

    private lateinit var device: UiDevice

    @Before
    fun setup() {
        runBlocking {
            FirebaseAuth.getInstance().signInAnonymously()
        }

        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.wakeUp()
        device.pressHome()
    }

    @Test
    fun `when HomeScreen is Success permission dialog appears`() {
        ActivityScenario.launch(MainActivity::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val allowButton = device.wait(
                Until.findObject(By.textContains(ENGLISH_ALLOW_WORD)),
                WAITING_TIME,
            ) ?: device.wait(
                Until.findObject(By.textContains(RUSSIAN_ALLOW_WORD)),
                WAITING_TIME,
            )

            assertTrue(allowButton != null)
        }
    }
}