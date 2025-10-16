package merail.life.mejourney

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.google.firebase.auth.FirebaseAuth
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import merail.life.home.content.navigation.ContentRoute
import merail.life.mejourney.navigation.CATEGORY_KEY
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class MejourneyNavHostTest {

    companion object {
        private const val CONTENT_ID = "home_cover_9"

        private const val CONTENT_TITLE = "Москва"

        private const val WAITING_TIME = 5_000L
    }

    private lateinit var context: Context

    private lateinit var device: UiDevice

    @Before
    fun setup() {
        runBlocking {
            FirebaseAuth.getInstance().signInAnonymously()
        }

        context = InstrumentationRegistry.getInstrumentation().targetContext

        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.wakeUp()
        device.pressHome()
    }

    @Test
    fun `navigate from push to ContentRoute changes destination`() {
        ActivityScenario.launch(MainActivity::class.java)

        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            putExtra(CATEGORY_KEY, ContentRoute.ROUTE_NAME)
            putExtra(ContentRoute.CONTENT_ID_KEY, CONTENT_ID)
        }

        context.startActivity(intent)

        val contentTitle = device.wait(
            Until.findObject(By.textContains(CONTENT_TITLE)),
            WAITING_TIME,
        )

        assertTrue(contentTitle != null)
    }
}