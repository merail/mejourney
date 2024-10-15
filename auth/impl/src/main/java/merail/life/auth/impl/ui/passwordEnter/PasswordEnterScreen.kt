package merail.life.auth.impl.ui.passwordEnter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import merail.life.auth.impl.R
import merail.life.auth.impl.ui.common.PasswordField
import merail.life.auth.impl.ui.passwordEnter.state.AuthByPasswordState
import merail.life.auth.impl.ui.passwordEnter.state.needToBlockUi
import merail.life.core.NavigationDestination
import merail.life.design.MejourneyTheme
import merail.life.design.styles.ButtonStyle

object PasswordEnterDestination : NavigationDestination {
    override val route = "passwordEnter"

    const val EMAIL_ARG = "email"

    val routeWithArgs = "$route/{$EMAIL_ARG}"
}

@Composable
fun PasswordEnterScreen(
    onError: (Throwable?) -> Unit,
    navigateToHome: () -> Unit,
    viewModel: PasswordEnterViewModel = hiltViewModel<PasswordEnterViewModel>(),
) {
    val state = viewModel.authByPasswordState.value
    when (state) {
        is AuthByPasswordState.Error -> LaunchedEffect(null) {
            onError(state.exception)
        }
        is AuthByPasswordState.Success -> LaunchedEffect(null) {
            navigateToHome()
        }
        is AuthByPasswordState.None,
        is AuthByPasswordState.Loading,
        is AuthByPasswordState.InvalidPassword,
        -> Unit
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
            ) {
                Text(
                    text = stringResource(R.string.password_enter_title),
                    style = MejourneyTheme.typography.displaySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 40.dp,
                            top = 40.dp,
                            end = 40.dp,
                        ),
                )

                Text(
                    text = stringResource(R.string.password_enter_description),
                    style = MejourneyTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 24.dp,
                            vertical = 20.dp,
                        ),
                )

                PasswordField(
                    passwordValueState = viewModel.passwordValueState,
                    onChange = remember {
                        {
                            viewModel.updatePassword(it)
                        }
                    },
                    imeAction = ImeAction.Done,
                    label = stringResource(R.string.password_enter_label),
                    errorText = stringResource(R.string.password_enter_validation_error),
                )
            }

            Button(
                onClick = remember
                {
                    {
                        viewModel.validatePassword()
                    }
                },
                colors = ButtonStyle.Primary.colors(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .height(64.dp),
            ) {
                if (state.needToBlockUi) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp),
                    )
                } else {
                    Text(
                        text = stringResource(R.string.password_enter_continue_button),
                        textAlign = TextAlign.Center,
                        style = MejourneyTheme.typography.titleMedium,
                    )
                }
            }
        }

        if (state.needToBlockUi) {
            Surface(
                color = Color.Transparent,
                content = {},
                modifier = Modifier
                    .fillMaxSize(),
            )
        }
    }
}