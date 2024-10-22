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
import merail.life.auth.impl.ui.emailInput.EmailInputDestination
import merail.life.auth.impl.ui.emailInput.EmailInputScreen
import merail.life.auth.impl.ui.otpInput.OtpInputDestination
import merail.life.auth.impl.ui.otpInput.OtpInputScreen
import merail.life.auth.impl.ui.passwordCreation.PasswordCreationDestination
import merail.life.auth.impl.ui.passwordCreation.PasswordCreationScreen
import merail.life.auth.impl.ui.passwordEnter.PasswordEnterDestination
import merail.life.auth.impl.ui.passwordEnter.PasswordEnterScreen
import merail.life.core.UnauthorizedException
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
        startDestination = SplashDestination.route,
        modifier = modifier,
    ) {
        composable(
            route = SplashDestination.route,
        ) {

            val navigateToAuth: (Throwable?) -> Unit = remember {
                {
                    navController.navigate(EmailInputDestination.routeWithArgs)
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
            route = EmailInputDestination.routeWithArgs,

            arguments = listOf(
                element = navArgument(EmailInputDestination.EMAIL_ARG) {
                    nullable = true
                    type = NavType.StringType
                },
            ),
        ) {
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
                    navController.navigate("${PasswordEnterDestination.route}/$it")
                }
            }

            val navigateToOtp: (String) -> Unit = remember {
                {
                    navController.navigate("${OtpInputDestination.route}/$it")
                }
            }

            EmailInputScreen(
                onError = navigateToError,
                navigateToPasswordEnter = navigateToPasswordEnter,
                navigateToOtp = navigateToOtp,
            )
        }
        composable(
            route = PasswordEnterDestination.routeWithArgs,
            arguments = listOf(
                element = navArgument(PasswordEnterDestination.EMAIL_ARG) {
                    type = NavType.StringType
                },
            ),
        ) {
            val navigateToError: (Throwable?) -> Unit = remember {
                {
                    navController.navigateToError(it)
                }
            }

            val navigateToBack: (String) -> Unit = remember {
                {
                    navController.navigate("${EmailInputDestination.route}?${EmailInputDestination.EMAIL_ARG}=$it")
                }
            }

            val navigateToHome: () -> Unit = remember {
                {
                    navController.navigate(HomeDestination.route)
                }
            }

            PasswordEnterScreen(
                navigateToBack = navigateToBack,
                onError = navigateToError,
                navigateToHome = navigateToHome,
            )
        }
        composable(
            route = OtpInputDestination.routeWithArgs,
            arguments = listOf(
                element = navArgument(OtpInputDestination.EMAIL_ARG) {
                    type = NavType.StringType
                },
            ),
        ) {

            val navigateToBack: (String) -> Unit = remember {
                {
                    navController.navigate("${EmailInputDestination.route}?${EmailInputDestination.EMAIL_ARG}=$it")
                }
            }

            val navigateToPassword: (String) -> Unit = remember {
                {
                    navController.navigate("${PasswordCreationDestination.route}/$it")
                }
            }

            OtpInputScreen(
                navigateToBack = navigateToBack,
                navigateToPassword = navigateToPassword,
            )
        }
        composable(
            route = PasswordCreationDestination.routeWithArgs,
            arguments = listOf(
                element = navArgument(PasswordCreationDestination.EMAIL_ARG) {
                    type = NavType.StringType
                },
            ),
        ) {
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
                    navController.navigate(HomeDestination.route)
                }
            }

            PasswordCreationScreen(
                onError = navigateToError,
                navigateToHome = navigateToHome,
            )
        }
        composable(
            route = HomeDestination.route,
        ) {
            BackHandler {
                context.activity?.moveTaskToBack(true)
            }

            val navigateToError: (Throwable?) -> Unit = remember {
                {
                    if (it is UnauthorizedException) {
                        navController.navigate(EmailInputDestination.routeWithArgs)
                    } else {
                        navController.navigateToError(it)
                    }
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