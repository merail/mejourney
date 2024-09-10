package merail.life.mejourney.navigation

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import merail.life.auth.ui.AuthDestination
import merail.life.auth.ui.AuthScreen
import merail.life.core.extensions.activity
import merail.life.data.model.SelectorFilterType
import merail.life.home.content.ContentDestination
import merail.life.home.content.ContentScreen
import merail.life.home.main.HomeDestination
import merail.life.home.main.HomeScreen
import merail.life.home.selector.SelectorDestination
import merail.life.home.selector.SelectorScreen
import merail.life.mejourney.error.ErrorDestination
import merail.life.mejourney.error.ErrorDialog
import merail.life.mejourney.error.ErrorType
import merail.life.splash.SplashDestination
import merail.life.splash.SplashScreen

private const val TAG = "MejourneyNavHost"

@Composable
internal fun MejourneyNavHost(
    navController: NavHostController,
    intentRoute: MutableState<String?>,
    modifier: Modifier = Modifier,
) {
    // TODO: Routing from push doesn't work without getting intentRouteValue here
    intentRoute.value?.let {
        Log.d(TAG, "Route from push: $it")
    }
    navController.addOnPushNotificationListener(
        intentRoute = intentRoute,
    )

    NavHost(
        navController = navController,
        startDestination = SplashDestination.route,
        modifier = modifier,
    ) {
        composable(
            route = SplashDestination.route,
        ) {

            val navigateToAuth: (Throwable?) -> Unit = remember {
                {
                    navController.navigate(AuthDestination.route)
                    it?.let {
                        navController.navigateToError(it)
                    }
                }
            }

            val navigateToHome: (Throwable?) -> Unit = remember {
                {
                    navController.navigate(HomeDestination.route)
                    it?.let {
                        navController.navigateToError(it)
                    }
                }
            }

            SplashScreen(
                navigateToAuth = navigateToAuth,
                navigateToHome = navigateToHome,
            )
        }
        composable(
            route = AuthDestination.route,
        ) {
            AuthScreen(
                onError = {
                    navController.navigateToError(it)
                },
                navigateToHome = {
                    navController.navigate(HomeDestination.route)
                },
            )
        }
        composable(
            route = HomeDestination.route,
        ) {
            val context = LocalContext.current
            BackHandler {
                context.activity?.finish()
            }

            val navigateToError: (Throwable?) -> Unit = remember {
                {
                    navController.navigateToError(it)
                }
            }

            val navigateToSelector: (SelectorFilterType?) -> Unit = remember {
                {
                    navController.navigate("${SelectorDestination.route}/$it")
                }
            }

            val navigateToContent: (String?) -> Unit = remember {
                {
                    navController.navigate("${ContentDestination.route}/$it")
                }
            }

            HomeScreen(
                onError = navigateToError,
                navigateToSelector = navigateToSelector,
                navigateToContent = navigateToContent,
            )
        }
        composable(
            route = SelectorDestination.routeWithArgs,
            arguments = listOf(
                element = navArgument(SelectorDestination.SELECTOR_FILTER_ARG) {
                    type = NavType.EnumType(SelectorFilterType::class.java)
                },
            ),
        ) {
            val navigateToError: (Throwable?) -> Unit = remember {
                {
                    navController.popBackStack()
                    navController.navigateToError(it)
                }
            }

            val navigateToContent: (String?) -> Unit = remember {
                {
                    navController.navigate("${ContentDestination.route}/$it")
                }
            }

            val navigateToContentImmediately: (String?) -> Unit = remember {
                {
                    navController.popBackStack()
                    navController.navigate("${ContentDestination.route}/$it")
                }
            }

            SelectorScreen(
                onError = navigateToError,
                navigateToContent = navigateToContent,
                navigateToContentImmediately = navigateToContentImmediately,
            )
        }
        composable(
            route = ContentDestination.routeWithArgs,
            arguments = listOf(
                element = navArgument(ContentDestination.CONTENT_ID_ARG) {
                    type = NavType.StringType
                },
            ),
        ) {
            val navigateToError: (Throwable?) -> Unit = remember {
                {
                    navController.popBackStack()
                    navController.navigateToError(it)
                }
            }

            ContentScreen(
                navigateToError = navigateToError,
            )
        }
        dialog(
            route = ErrorDestination.routeWithArgs,
            dialogProperties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
            ),
            arguments = listOf(
                element = navArgument(ErrorDestination.ERROR_TYPE_ARG) {
                    type = NavType.EnumType(ErrorType::class.java)
                },
            ),
        ) {
            val onDismiss: () -> Unit = remember {
                {
                    navController.popBackStack()
                }
            }

            ErrorDialog(
                errorType = it.errorType,
                onDismiss = onDismiss,
            )
        }
    }
}