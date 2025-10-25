package merail.life.mejourney

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import merail.life.core.constants.TestHomeElements
import merail.life.core.constants.TestTags
import merail.life.core.errors.ErrorType
import merail.life.data.impl.di.DataModule
import merail.life.data.test.repository.TestDataRepository
import merail.life.mejourney.navigation.MejourneyNavHost
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * Instrumented UI test suite that verifies proper error handling and UI state behavior
 * in various initialization and navigation scenarios within the Mejourney app.
 *
 * These tests use Hilt for dependency injection and Jetpack Compose testing APIs
 * to validate that appropriate error dialogs are displayed and corresponding
 * screens (Home, Selector, Content) are shown or hidden depending on the error type.
 */
@HiltAndroidTest
@UninstallModules(DataModule::class)
internal class ErrorsHandlingTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var testDataRepository: TestDataRepository

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        hiltRule.inject()
    }

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
     * Verifies that when an error occurs during HomeScreen initial loading,
     * the dismiss error dialog is displayed to the user.
     */
    @Test
    fun `when HomeScreen init loading error dismiss box appears`() {
        testDataRepository.setShouldReturnErrorInGetHomeElements(true)

        composeTestRule.setContent {
            MejourneyNavHost(
                intentRoute = null,
                errorType = null,
            )
        }

        composeTestRule.onNodeWithTag(TestTags.ERROR_DIALOG_CONTAINER).assertIsDisplayed()
    }

    /**
     * Verifies that when an error occurs during ContentScreen initialization,
     * the error dialog is shown, the ContentScreen remains hidden,
     * and the user is returned to the HomeScreen.
     */
    @Test
    fun `when ContentScreen init loading error dismiss box appears`() {
        composeTestRule.setContent {
            MejourneyNavHost(
                intentRoute = null,
                errorType = null,
            )
        }

        composeTestRule.onNodeWithTag("${TestTags.COVER_IMAGE}_${TestHomeElements.ID_1}").performClick()
        composeTestRule.onNodeWithTag(TestTags.ERROR_DIALOG_CONTAINER).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.CONTENT_SCREEN_CONTAINER).assertIsNotDisplayed()
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_CONTAINER).assertIsDisplayed()
    }

    /**
     * Ensures that when a HomeScreen tab fails to load data from the database,
     * the error dialog is displayed to the user.
     */
    @Test
    fun `when HomeScreen tab loading error dismiss box appears`() {
        testDataRepository.setShouldReturnErrorInGetHomeElementsFromDatabase(true)

        composeTestRule.setContent {
            MejourneyNavHost(
                intentRoute = null,
                errorType = null,
            )
        }

        composeTestRule.onNodeWithTag("${TestTags.HOME_TAB}_2").performClick()
        composeTestRule.onNodeWithTag(TestTags.ERROR_DIALOG_CONTAINER).assertIsDisplayed()
    }

    /**
     * Verifies that when the SelectorScreen fails to load its initial data,
     * an error dialog is shown, the SelectorScreen is hidden,
     * and the app returns to the HomeScreen.
     */
    @Test
    fun `when SelectorScreen init loading error dismiss box appears`() {
        composeTestRule.setContent {
            MejourneyNavHost(
                intentRoute = null,
                errorType = null,
            )
        }

        composeTestRule.onNodeWithTag("${TestTags.HOME_TAB}_1").performClick()

        testDataRepository.setShouldReturnErrorInGetHomeElementsFromDatabase(true)

        composeTestRule.onNodeWithTag("${TestTags.COVER_IMAGE}_${TestHomeElements.ID_1}").performClick()
        composeTestRule.onNodeWithTag(TestTags.ERROR_DIALOG_CONTAINER).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.SELECTOR_SCREEN_CONTAINER).assertIsNotDisplayed()
        composeTestRule.onNodeWithTag(TestTags.HOME_SCREEN_CONTAINER).assertIsDisplayed()
    }
}