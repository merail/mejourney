package merail.life.mejourney

import android.os.Build
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import merail.life.auth.api.IAuthRepository
import merail.life.data.test.di.TestDataModule
import merail.life.mejourney.MejourneyNavGraphTest.Companion.WAITING_TIME
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * Instrumented UI test for verifying the behavior of [MainActivity].
 *
 * It uses [UiDevice] for interacting with system UI elements and
 * performs an anonymous Firebase authentication before starting the test.
 */
@HiltAndroidTest
@UninstallModules(TestDataModule::class)
internal class MainActivityTest {

    companion object {
        private const val ENGLISH_ALLOW_WORD = "ALLOW"
        private const val RUSSIAN_ALLOW_WORD = "РАЗРЕШИТЬ"
    }

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var authRepository: IAuthRepository

    private lateinit var device: UiDevice

    @Before
    fun setup() {
        hiltRule.inject()

        runBlocking {
            authRepository.authorize()
        }

        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.wakeUp()
        device.pressHome()
    }

    /**
     * Verifies that when the HomeScreen loads successfully on Android 13+,
     * a system permission dialog appears (with "ALLOW" or "РАЗРЕШИТЬ" button visible).
     */
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