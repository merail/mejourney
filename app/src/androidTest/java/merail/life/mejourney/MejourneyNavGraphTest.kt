package merail.life.mejourney

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import merail.life.core.constants.TestHomeElements
import merail.life.core.constants.TestTags
import merail.life.core.errors.ErrorType
import merail.life.core.navigation.NavigationRoute
import merail.life.home.content.navigation.ContentRoute
import merail.life.mejourney.navigation.MejourneyNavHost
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MejourneyNavGraphTest {

    companion object {
        private const val WAITING_TIME = 5_000L
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    /**
     * Verifies that when the app initializes with an internet connection error,
     * the corresponding error dialog is displayed,
     * and the HomeScreen content is not visible.
     */
    @Test
    fun `when init loading internet connection error dismiss box appears`() {
        composeTestRule.setContent {
            MejourneyNavHost(
                intentRoute = null,
                errorType = ErrorType.INTERNET_CONNECTION,
            )
        }

        composeTestRule.onNodeWithText(
            text = context.getString(merail.life.error.R.string.error_internet_connection_subtitle),
        ).assertIsDisplayed()

        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_CONTAINER).assertIsNotDisplayed()
    }

    /**
     * Ensures that when the app initializes with a common (non-network) error,
     * the corresponding error dialog is shown,
     * and the HomeScreen content remains hidden.
     */
    @Test
    fun `when init loading common error dismiss box appears`() {
        composeTestRule.setContent {
            MejourneyNavHost(
                intentRoute = null,
                errorType = ErrorType.OTHER,
            )
        }

        composeTestRule.onNodeWithText(
            text = context.getString(merail.life.error.R.string.error_common_subtitle),
        ).assertIsDisplayed()

        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_CONTAINER).assertIsNotDisplayed()
    }

    /**
     * Verifies that when the SelectorScreen contains only one available element,
     * selecting it immediately opens the corresponding ContentScreen
     * without requiring any additional navigation steps.
     */
    @Test
    fun `SelectorScreen with one element open ContentScreen straight`() {
        lateinit var navController: NavHostController

        composeTestRule.setContent {
            navController = rememberNavController()

            MejourneyNavHost(
                navController = navController,
                intentRoute = null,
                errorType = null,
            )
        }

        composeTestRule.waitUntil(WAITING_TIME) {
            composeTestRule.onNodeWithTag("${TestTags.HOME_TAB}_1").isDisplayed()
        }

        composeTestRule.onNodeWithTag("${TestTags.HOME_TAB}_1").performClick()

        composeTestRule.waitUntil(WAITING_TIME) {
            composeTestRule.onNodeWithTag("${TestTags.COVER_IMAGE}_${TestHomeElements.ID_7}").isDisplayed()
        }

        composeTestRule.onNodeWithTag("${TestTags.COVER_IMAGE}_${TestHomeElements.ID_7}").performClick()

        composeTestRule.waitUntil(WAITING_TIME) {
            composeTestRule.onNodeWithText(TestHomeElements.CAPPADOCIA_CONTENT_TITLE).isDisplayed()
        }
    }

    /**
     * Verifies that when the app starts with a push intent containing a ContentRoute,
     * the navigation correctly opens the corresponding content screen (e.g., "Карелия").
     */
    @Test
    fun `navigate from push to ContentRoute when app is closed`() {
        composeTestRule.setContent {
            MejourneyNavHost(
                intentRoute = remember {
                    mutableStateOf(ContentRoute(TestHomeElements.ID_2))
                },
                errorType = null,
            )
        }

        composeTestRule.waitUntil(WAITING_TIME) {
            composeTestRule.onNodeWithText(TestHomeElements.KARELIA_CONTENT_TITLE).isDisplayed()
        }
    }

    /**
     * Ensures that when a push intent with a ContentRoute is received while the HomeScreen is already open,
     * the navigation correctly transitions to the corresponding content screen (e.g., "Карелия").
     */
    @Test
    fun `navigate from push to ContentRoute when HomeScreen is opened`() {
        composeTestRule.setContent {
            var intentRoute: MutableState<NavigationRoute?> = remember {
                mutableStateOf(null)
            }

            MejourneyNavHost(
                intentRoute = intentRoute,
                errorType = null,
            )

            intentRoute.value = ContentRoute(TestHomeElements.ID_2)
        }

        composeTestRule.waitUntil(WAITING_TIME) {
            composeTestRule.onNodeWithText(TestHomeElements.KARELIA_CONTENT_TITLE).isDisplayed()
        }
    }

    /**
     * Verifies that when the SelectorScreen is currently open and a push intent with a ContentRoute is received,
     * the app navigates to the corresponding content screen (e.g., "Карелия"),
     * and after navigating back, the SelectorScreen is correctly displayed again.
     */
    @Test
    fun `navigate from push to ContentRoute when SelectorScreen is opened`() {
        lateinit var navController: NavHostController

        var intentRoute = mutableStateOf<NavigationRoute?>(null)

        composeTestRule.setContent {
            navController = rememberNavController()

            MejourneyNavHost(
                navController = navController,
                intentRoute = intentRoute,
                errorType = null,
            )
        }

        composeTestRule.waitUntil(WAITING_TIME) {
            composeTestRule.onNodeWithTag("${TestTags.HOME_TAB}_1").isDisplayed()
        }

        composeTestRule.onNodeWithTag("${TestTags.HOME_TAB}_1").performClick()

        composeTestRule.waitUntil(WAITING_TIME) {
            composeTestRule.onNodeWithTag("${TestTags.COVER_IMAGE}_${TestHomeElements.ID_1}").isDisplayed()
        }

        composeTestRule.onNodeWithTag("${TestTags.COVER_IMAGE}_${TestHomeElements.ID_1}").performClick()

        intentRoute.value = ContentRoute(TestHomeElements.ID_2)

        composeTestRule.waitUntil(WAITING_TIME) {
            composeTestRule.onNodeWithText(TestHomeElements.KARELIA_CONTENT_TITLE).isDisplayed()
        }

        composeTestRule.runOnUiThread {
            navController.popBackStack()
        }

        composeTestRule.onNodeWithTag(TestTags.SELECTOR_SCREEN_CONTAINER).assertIsDisplayed()
    }

    /**
     * Ensures that when the SelectorScreen is open and a push intent targets a different ContentRoute
     * than the one currently displayed, the navigation transitions to the new content screen,
     * and upon navigating back, the previously opened content screen is shown again.
     */
    @Test
    fun `navigate from push to ContentRoute when not same content from SelectorScreen is opened`() {
        lateinit var navController: NavHostController

        var intentRoute = mutableStateOf<NavigationRoute?>(null)

        composeTestRule.setContent {
            navController = rememberNavController()

            MejourneyNavHost(
                navController = navController,
                intentRoute = intentRoute,
                errorType = null,
            )
        }

        composeTestRule.waitUntil(WAITING_TIME) {
            composeTestRule.onNodeWithTag("${TestTags.HOME_TAB}_1").isDisplayed()
        }

        composeTestRule.onNodeWithTag("${TestTags.HOME_TAB}_1").performClick()

        composeTestRule.waitUntil(WAITING_TIME) {
            composeTestRule.onNodeWithTag("${TestTags.COVER_IMAGE}_${TestHomeElements.ID_1}").isDisplayed()
        }

        composeTestRule.onNodeWithTag("${TestTags.COVER_IMAGE}_${TestHomeElements.ID_1}").performClick()

        composeTestRule.waitUntil(WAITING_TIME) {
            composeTestRule.onNodeWithTag("${TestTags.COVER_IMAGE}_${TestHomeElements.ID_1}").isDisplayed()
        }

        composeTestRule.onNodeWithTag("${TestTags.COVER_IMAGE}_${TestHomeElements.ID_1}").performClick()

        intentRoute.value = ContentRoute(TestHomeElements.ID_2)

        composeTestRule.waitUntil(WAITING_TIME) {
            composeTestRule.onNodeWithText(TestHomeElements.KARELIA_CONTENT_TITLE).isDisplayed()
        }

        composeTestRule.runOnUiThread {
            navController.popBackStack()
        }

        composeTestRule.onNodeWithText(TestHomeElements.MOSCOW_CONTENT_TITLE).assertIsDisplayed()
    }

    /**
     * Verifies that when the SelectorScreen is open and a push intent targets the same ContentRoute
     * as the one currently displayed, no duplicate navigation occurs,
     * and returning back correctly shows the SelectorScreen again.
     */
    @Test
    fun `navigate from push to ContentRoute when same content from SelectorScreen is opened`() {
        lateinit var navController: NavHostController

        var intentRoute = mutableStateOf<NavigationRoute?>(null)

        composeTestRule.setContent {
            navController = rememberNavController()

            MejourneyNavHost(
                navController = navController,
                intentRoute = intentRoute,
                errorType = null,
            )
        }

        composeTestRule.waitUntil(WAITING_TIME) {
            composeTestRule.onNodeWithTag("${TestTags.HOME_TAB}_1").isDisplayed()
        }

        composeTestRule.onNodeWithTag("${TestTags.HOME_TAB}_1").performClick()

        composeTestRule.waitUntil(WAITING_TIME) {
            composeTestRule.onNodeWithTag("${TestTags.COVER_IMAGE}_${TestHomeElements.ID_1}").isDisplayed()
        }

        composeTestRule.onNodeWithTag("${TestTags.COVER_IMAGE}_${TestHomeElements.ID_1}").performClick()

        composeTestRule.waitUntil(WAITING_TIME) {
            composeTestRule.onNodeWithTag("${TestTags.COVER_IMAGE}_${TestHomeElements.ID_1}").isDisplayed()
        }

        composeTestRule.onNodeWithTag("${TestTags.COVER_IMAGE}_${TestHomeElements.ID_1}").performClick()

        intentRoute.value = ContentRoute(TestHomeElements.ID_1)

        composeTestRule.waitUntil(WAITING_TIME) {
            composeTestRule.onNodeWithText(TestHomeElements.MOSCOW_CONTENT_TITLE).isDisplayed()
        }

        composeTestRule.runOnUiThread {
            navController.popBackStack()
        }

        composeTestRule.onNodeWithTag(TestTags.SELECTOR_SCREEN_CONTAINER).assertIsDisplayed()
    }
}