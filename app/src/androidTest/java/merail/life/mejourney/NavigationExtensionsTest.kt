package merail.life.mejourney

import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import merail.life.home.content.navigation.ContentRoute
import merail.life.home.main.navigation.HomeRoute
import merail.life.home.selector.navigation.SelectorRoute
import merail.life.mejourney.navigation.CATEGORY_KEY
import merail.life.mejourney.navigation.getRouteIfExists
import merail.life.mejourney.navigation.navigateFromPush
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationExtensionsTest {

    companion object {
        private const val CONTENT_ID = "42"
        private const val DIFFERENT_CONTENT_ID = "123"
    }

    @Test
    fun `getRouteIfExists returns null when category is missing`() {
        val intent = Intent()

        val route = intent.getRouteIfExists()

        assertEquals(null, route)
    }

    @Test
    fun `getRouteIfExists returns null when content id is missing`() {
        val intent = Intent().apply {
            putExtra(CATEGORY_KEY, ContentRoute.ROUTE_NAME)
        }

        val route = intent.getRouteIfExists()

        assertEquals(null, route)
    }

    @Test
    fun `getRouteIfExists returns ContentRoute when category and contentId match`() {
        val intent = Intent().apply {
            putExtra(CATEGORY_KEY, ContentRoute.ROUTE_NAME)
            putExtra(ContentRoute.CONTENT_ID_KEY, CONTENT_ID)
        }

        val route = intent.getRouteIfExists()

        assertTrue(route is ContentRoute)
        assertEquals(CONTENT_ID, (route as ContentRoute).contentId)
    }

    @Test
    fun `navigateFromPush navigates from Home`() {
        val navController = mockk<NavController>(relaxed = true)
        val backStackEntry = mockk<NavBackStackEntry>()
        val destination = mockk<NavDestination>()

        every { navController.currentBackStackEntry } returns backStackEntry
        every { backStackEntry.destination } returns destination
        every { backStackEntry.arguments } returns Bundle()
        every { destination.route } returns HomeRoute::class.qualifiedName

        val intentRoute = ContentRoute(CONTENT_ID)

        navController.navigateFromPush(intentRoute)

        verify { navController.navigate(intentRoute) }
    }

    @Test
    fun `navigateFromPush navigates from Selector`() {
        val navController = mockk<NavController>(relaxed = true)
        val backStackEntry = mockk<NavBackStackEntry>()
        val destination = mockk<NavDestination>()

        every { navController.currentBackStackEntry } returns backStackEntry
        every { backStackEntry.destination } returns destination
        every { backStackEntry.arguments } returns Bundle()
        every { destination.route } returns SelectorRoute::class.qualifiedName

        val intentRoute = ContentRoute(CONTENT_ID)

        navController.navigateFromPush(intentRoute)

        verify { navController.navigate(intentRoute) }
    }

    @Test
    fun `navigateFromPush does not navigates when same Content`() {
        val navController = mockk<NavController>(relaxed = true)
        val backStackEntry = mockk<NavBackStackEntry>()
        val destination = mockk<NavDestination>()
        val args = Bundle().apply {
            putString(ContentRoute.CONTENT_ID_KEY, CONTENT_ID)
        }

        every { navController.currentBackStackEntry } returns backStackEntry
        every { backStackEntry.destination } returns destination
        every { backStackEntry.arguments } returns args
        every { destination.route } returns ContentRoute::class.qualifiedName

        val intentRoute = ContentRoute(CONTENT_ID)

        navController.navigateFromPush(intentRoute)

        verify(exactly = 0) { navController.navigate(any<ContentRoute>()) }
    }

    @Test
    fun `navigateFromPush does not navigates when different Content`() {
        val navController = mockk<NavController>(relaxed = true)
        val backStackEntry = mockk<NavBackStackEntry>()
        val destination = mockk<NavDestination>()
        val args = Bundle().apply {
            putString(ContentRoute.CONTENT_ID_KEY, CONTENT_ID)
        }

        every { navController.currentBackStackEntry } returns backStackEntry
        every { backStackEntry.destination } returns destination
        every { backStackEntry.arguments } returns args
        every { destination.route } returns ContentRoute::class.qualifiedName

        val intentRoute = ContentRoute(DIFFERENT_CONTENT_ID)

        navController.navigateFromPush(intentRoute)

        verify { navController.navigate(intentRoute) }
    }
}


