package merail.life.mejourney

import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import merail.life.core.constants.TestHomeElements
import merail.life.home.content.navigation.ContentRoute
import merail.life.home.main.navigation.HomeRoute
import merail.life.home.selector.navigation.SelectorRoute
import merail.life.mejourney.navigation.CATEGORY_KEY
import merail.life.mejourney.navigation.getRouteIfExists
import merail.life.mejourney.navigation.navigateFromPush
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for navigation extension functions in the app, verifying
 * intent parsing and conditional navigation logic in Compose Navigation.
 */
internal class NavigationExtensionsTest {

    /**
     * Ensures that [Intent.getRouteIfExists] returns null
     * when no category extra is present in the intent.
     */
    @Test
    fun `getRouteIfExists returns null when category is missing`() {
        val intent = Intent()

        val route = intent.getRouteIfExists()

        assertEquals(null, route)
    }

    /**
     * Ensures that [Intent.getRouteIfExists] returns null
     * when the category is present but content ID is missing.
     */
    @Test
    fun `getRouteIfExists returns null when content id is missing`() {
        val intent = Intent().apply {
            putExtra(CATEGORY_KEY, ContentRoute.ROUTE_NAME)
        }

        val route = intent.getRouteIfExists()

        assertEquals(null, route)
    }

    /**
     * Verifies that [Intent.getRouteIfExists] correctly returns a [ContentRoute]
     * when both category and content ID extras are present.
     */
    @Test
    fun `getRouteIfExists returns ContentRoute when category and contentId match`() {
        val intent = Intent().apply {
            putExtra(CATEGORY_KEY, ContentRoute.ROUTE_NAME)
            putExtra(ContentRoute.CONTENT_ID_KEY, TestHomeElements.ID_1)
        }

        val route = intent.getRouteIfExists()

        assertTrue(route is ContentRoute)
        assertEquals(TestHomeElements.ID_1, (route as ContentRoute).contentId)
    }

    /**
     * Verifies that [NavController.navigateFromPush] navigates
     * correctly when the current destination is HomeScreen.
     */
    @Test
    fun `navigateFromPush navigates from Home`() {
        val navController = mockk<NavController>(relaxed = true)
        val backStackEntry = mockk<NavBackStackEntry>()
        val destination = mockk<NavDestination>()

        every { navController.currentBackStackEntry } returns backStackEntry
        every { backStackEntry.destination } returns destination
        every { backStackEntry.arguments } returns Bundle()
        every { destination.route } returns HomeRoute::class.qualifiedName

        val intentRoute = ContentRoute(TestHomeElements.ID_1)

        navController.navigateFromPush(intentRoute)

        verify { navController.navigate(intentRoute) }
    }

    /**
     * Verifies that [NavController.navigateFromPush] navigates
     * correctly when the current destination is SelectorScreen.
     */
    @Test
    fun `navigateFromPush navigates from Selector`() {
        val navController = mockk<NavController>(relaxed = true)
        val backStackEntry = mockk<NavBackStackEntry>()
        val destination = mockk<NavDestination>()

        every { navController.currentBackStackEntry } returns backStackEntry
        every { backStackEntry.destination } returns destination
        every { backStackEntry.arguments } returns Bundle()
        every { destination.route } returns SelectorRoute::class.qualifiedName

        val intentRoute = ContentRoute(TestHomeElements.ID_1)

        navController.navigateFromPush(intentRoute)

        verify { navController.navigate(intentRoute) }
    }

    /**
     * Ensures that [NavController.navigateFromPush] does not navigate
     * when the current destination is ContentScreen and the content ID
     * matches the push intent.
     */
    @Test
    fun `navigateFromPush does not navigates when same Content`() {
        val navController = mockk<NavController>(relaxed = true)
        val backStackEntry = mockk<NavBackStackEntry>()
        val destination = mockk<NavDestination>()
        val args = Bundle().apply {
            putString(ContentRoute.CONTENT_ID_KEY, TestHomeElements.ID_1)
        }

        every { navController.currentBackStackEntry } returns backStackEntry
        every { backStackEntry.destination } returns destination
        every { backStackEntry.arguments } returns args
        every { destination.route } returns ContentRoute::class.qualifiedName

        val intentRoute = ContentRoute(TestHomeElements.ID_1)

        navController.navigateFromPush(intentRoute)

        verify(exactly = 0) { navController.navigate(any<ContentRoute>()) }
    }

    /**
     * Ensures that [NavController.navigateFromPush] navigates
     * when the current destination is ContentScreen but the content ID
     * differs from the push intent.
     */
    @Test
    fun `navigateFromPush does not navigates when different Content`() {
        val navController = mockk<NavController>(relaxed = true)
        val backStackEntry = mockk<NavBackStackEntry>()
        val destination = mockk<NavDestination>()
        val args = Bundle().apply {
            putString(ContentRoute.CONTENT_ID_KEY, TestHomeElements.ID_1)
        }

        every { navController.currentBackStackEntry } returns backStackEntry
        every { backStackEntry.destination } returns destination
        every { backStackEntry.arguments } returns args
        every { destination.route } returns ContentRoute::class.qualifiedName

        val intentRoute = ContentRoute(TestHomeElements.ID_2)

        navController.navigateFromPush(intentRoute)

        verify { navController.navigate(intentRoute) }
    }
}


