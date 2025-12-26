package merail.life.home

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.collections.immutable.toImmutableList
import merail.life.data.api.model.HomeElementModel
import merail.life.domain.TestHomeElements
import merail.life.domain.TestTags
import merail.life.home.main.HomeContent
import merail.life.home.main.HomeLoadingState
import merail.life.home.model.toHomeItems
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * `HomeScreenTest` Instrumented UI tests for the Home screen using Jetpack Compose.
 * These tests verify the visual representation of different loading states,
 * user interactions like long-pressing elements, and the correct switching of content layouts
 * when interacting with the navigation tabs.
 */
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val items = listOf(
        HomeElementModel(
            id = TestHomeElements.ID_1,
            year = TestHomeElements.YEAR_23,
            country = TestHomeElements.COUNTRY_RUSSIA,
            place = TestHomeElements.PLACE_MOSCOW,
            title = TestHomeElements.TITLE_1,
            description = TestHomeElements.DESCRIPTION_1,
            imageUrl = TestHomeElements.URL_1,
        ),
    ).toHomeItems().toImmutableList()

    /**
     * Ensures that a full-screen (global) loader is displayed during the initial data fetch
     * and is correctly replaced by the content once the data is loaded.
     */
    @Test
    fun `global loader is visible when first launch`() {
        var state = mutableStateOf<HomeLoadingState>(HomeLoadingState.Loading())

        composeTestRule.setContent {
            HomeContent(
                state = state.value,
                isSnowfallEnabled = false,
                navigateToSelector = {},
            )
        }

        composeTestRule.onNodeWithTag(TestTags.GLOBAL_LOADER).assertIsDisplayed()

        state.value = HomeLoadingState.Success(items)

        composeTestRule.onNodeWithTag(TestTags.GLOBAL_LOADER).assertIsNotDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TOP_LOADER).assertIsNotDisplayed()
        composeTestRule.onNodeWithTag("${TestTags.COVER_IMAGE}_${TestHomeElements.ID_1}").assertIsDisplayed()
    }

    /**
     * Verifies that when the screen already has data but is performing a background update,
     * a non-intrusive top loader is shown instead of the global one.
     */
    @Test
    fun `top loader is visible when not first launch`() {
        var state = mutableStateOf<HomeLoadingState>(
            value = HomeLoadingState.Loading(items),
        )

        composeTestRule.setContent {
            HomeContent(
                state = state.value,
                isSnowfallEnabled = false,
                navigateToSelector = {},
            )
        }

        composeTestRule.onNodeWithTag(TestTags.GLOBAL_LOADER).assertIsNotDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TOP_LOADER).assertIsDisplayed()
        composeTestRule.onNodeWithTag("${TestTags.COVER_IMAGE}_${TestHomeElements.ID_1}").assertIsDisplayed()

        state.value = HomeLoadingState.Success(items)

        composeTestRule.onNodeWithTag(TestTags.GLOBAL_LOADER).assertIsNotDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TOP_LOADER).assertIsNotDisplayed()
        composeTestRule.onNodeWithTag("${TestTags.COVER_IMAGE}_${TestHomeElements.ID_1}").assertIsDisplayed()
    }

    /**
     * Validates that performing a long-press on a list item correctly triggers
     * the display of supplementary information, such as the title and description.
     */
    @Test
    fun `additional info is shown when long click on element`() {
        var state = mutableStateOf<HomeLoadingState>(
            value = HomeLoadingState.Success(items),
        )

        composeTestRule.setContent {
            HomeContent(
                state = state.value,
                isSnowfallEnabled = false,
                navigateToSelector = {},
            )
        }

        composeTestRule.onNodeWithTag(
            testTag = "${TestTags.COVER_IMAGE}_${TestHomeElements.ID_1}",
        ).performTouchInput {
            longClick()
        }

        composeTestRule.onNodeWithText(TestHomeElements.TITLE_1).assertIsDisplayed()
        composeTestRule.onNodeWithText(TestHomeElements.DESCRIPTION_1).assertIsDisplayed()
    }

    /**
     * Confirms that clicking on different tabs (Years, Countries, Places)
     * correctly swaps the UI container to the corresponding list type.
     */
    @Test
    fun `home content changes by click on tab`() {
        val state = mutableStateOf(HomeLoadingState.Success(items))

        composeTestRule.setContent {
            HomeContent(
                state = state.value,
                isSnowfallEnabled = false,
                navigateToSelector = {},
            )
        }

        composeTestRule.onNodeWithTag(TestTags.COMMON_LIST).assertIsDisplayed()

        composeTestRule.onNodeWithTag("${TestTags.HOME_TAB}_2").performClick()

        composeTestRule.onNodeWithTag(TestTags.PLACES_LIST).assertIsDisplayed()

        composeTestRule.onNodeWithTag("${TestTags.HOME_TAB}_1").performClick()

        composeTestRule.onNodeWithTag(TestTags.COUNTRIES_LIST).assertIsDisplayed()

        composeTestRule.onNodeWithTag("${TestTags.HOME_TAB}_0").performClick()

        composeTestRule.onNodeWithTag(TestTags.YEARS_LIST).assertIsDisplayed()
    }
}