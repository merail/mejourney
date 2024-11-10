package merail.life.navigation.graph

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import merail.life.auth.impl.ui.emailInput.EmailInputScreen
import merail.life.auth.impl.ui.otpInput.OtpInputScreen
import merail.life.auth.impl.ui.passwordCreation.PasswordCreationScreen
import merail.life.auth.impl.ui.passwordEnter.PasswordEnterScreen
import merail.life.core.extensions.activity
import merail.life.data.model.SelectorFilterType
import merail.life.home.content.ContentScreen
import merail.life.home.main.HomeScreen
import merail.life.home.selector.SelectorScreen
import merail.life.navigation.domain.NavigationRoute
import merail.life.navigation.domain.addOnPushNotificationListener
import merail.life.navigation.domain.error.ErrorDialog
import merail.life.navigation.domain.errorType
import merail.life.navigation.domain.navigateToError
import merail.life.splash.SplashScreen

private const val TAG = "MejourneyNavHost"

@Composable
fun MejourneyNavHost(
    navController: NavHostController,
    intentRoute: MutableState<NavigationRoute?>,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    // TODO: Routing from push doesn't work without getting intentRouteValue here
    intentRoute.value?.let {
        Log.d(TAG, "Route from push: $it")
    }
    navController.addOnPushNotificationListener(
        intentRoute = intentRoute,
    )

    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Splash,
        modifier = modifier,
    ) {
        composable<NavigationRoute.Splash> {

            val navigateToError: (Throwable?) -> Unit = remember {
                {
                    navController.navigateToError(it)
                }
            }

            val navigateToAuth: (Throwable?) -> Unit = remember {
                {
                    navController.navigate(NavigationRoute.Email(null))
                    it?.let {
                        navController.navigateToError(it)
                    }
                }
            }

            val navigateToHome: (Throwable?) -> Unit = remember {
                {
                    navController.navigate(NavigationRoute.Home)
                    it?.let {
                        navController.navigateToError(it)
                    }
                }
            }

            SplashScreen(
                onError = navigateToError,
                navigateToAuth = navigateToAuth,
                navigateToHome = navigateToHome,
            )
        }
        composable<NavigationRoute.Email> {
            BackHandler {
                context.activity?.moveTaskToBack(true)
            }

            val navigateToError: (Throwable?) -> Unit = remember {
                {
                    navController.navigateToError(it)
                }
            }

            val navigateToPasswordEnter: (String) -> Unit = remember {
                {
                    navController.navigate(NavigationRoute.PasswordEnter(it))
                }
            }

            val navigateToOtp: (String) -> Unit = remember {
                {
                    navController.navigate(NavigationRoute.OtpInput(it))
                }
            }

            EmailInputScreen(
                onError = navigateToError,
                navigateToPasswordEnter = navigateToPasswordEnter,
                navigateToOtp = navigateToOtp,
            )
        }
        composable<NavigationRoute.PasswordEnter> {
            val navigateToError: (Throwable?) -> Unit = remember {
                {
                    navController.navigateToError(it)
                }
            }

            val navigateToBack: (String) -> Unit = remember {
                {
                    navController.navigate(NavigationRoute.Email(it))
                }
            }

            val navigateToHome: () -> Unit = remember {
                {
                    navController.navigate(NavigationRoute.Home)
                }
            }

            PasswordEnterScreen(
                navigateToBack = navigateToBack,
                onError = navigateToError,
                navigateToHome = navigateToHome,
            )
        }
        composable<NavigationRoute.OtpInput> {

            val navigateToBack: (String) -> Unit = remember {
                {
                    navController.navigate(NavigationRoute.Email(it))
                }
            }

            val navigateToPassword: (String) -> Unit = remember {
                {
                    navController.navigate(NavigationRoute.PasswordCreation(it))
                }
            }

            OtpInputScreen(
                navigateToBack = navigateToBack,
                navigateToPassword = navigateToPassword,
            )
        }
        composable<NavigationRoute.PasswordCreation> {
            BackHandler {
                context.activity?.moveTaskToBack(true)
            }

            val navigateToError: (Throwable?) -> Unit = remember {
                {
                    navController.navigateToError(it)
                }
            }

            val navigateToHome: () -> Unit = remember {
                {
                    navController.navigate(NavigationRoute.Home)
                }
            }

            PasswordCreationScreen(
                onError = navigateToError,
                navigateToHome = navigateToHome,
            )
        }
        composable<NavigationRoute.Home> {
            BackHandler {
                context.activity?.moveTaskToBack(true)
            }

            val navigateToError: (Throwable?) -> Unit = remember {
                {
                    navController.navigateToError(it)
                }
            }

            val navigateToSelector: (SelectorFilterType) -> Unit = remember {
                {
                    navController.navigate(NavigationRoute.Selector(it))
                }
            }

            val navigateToContent: (String?) -> Unit = remember {
                {
                    navController.navigate(NavigationRoute.Content(it))
                }
            }

            HomeScreen(
                onError = navigateToError,
                navigateToSelector = navigateToSelector,
                navigateToContent = navigateToContent,
            )
        }
        composable<NavigationRoute.Selector> {
            val navigateToError: (Throwable?) -> Unit = remember {
                {
                    navController.popBackStack()
                    navController.navigateToError(it)
                }
            }

            val navigateToContent: (String?) -> Unit = remember {
                {
                    navController.navigate(NavigationRoute.Content(it))
                }
            }

            val navigateToContentImmediately: (String?) -> Unit = remember {
                {
                    navController.popBackStack()
                    navController.navigate(NavigationRoute.Content(it))
                }
            }

            SelectorScreen(
                onError = navigateToError,
                navigateToContent = navigateToContent,
                navigateToContentImmediately = navigateToContentImmediately,
            )
        }
        composable<NavigationRoute.Content> {
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
        dialog<NavigationRoute.Error>(
            dialogProperties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
            ),
        ) {

            BackHandler {
                navController.popBackStack()
            }

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