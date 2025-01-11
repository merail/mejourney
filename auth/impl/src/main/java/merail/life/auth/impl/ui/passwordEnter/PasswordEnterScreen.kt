package merail.life.auth.impl.ui.passwordEnter

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import merail.life.auth.impl.R
import merail.life.auth.impl.ui.common.PasswordField
import merail.life.auth.impl.ui.passwordEnter.state.AuthByPasswordState
import merail.life.auth.impl.ui.passwordEnter.state.needToBlockUi
import merail.life.design.MejourneyTheme
import merail.life.design.components.BlockingSurface
import merail.life.design.components.ContinueButton

@Composable
fun PasswordEnterContainer(
    onError: (Throwable?) -> Unit,
    navigateToBack: (String) -> Unit,
    navigateToHome: () -> Unit,
) = PasswordEnterScreen(
    onError = onError,
    navigateToBack = navigateToBack,
    navigateToHome = navigateToHome,
)

@Composable
internal fun PasswordEnterScreen(
    onError: (Throwable?) -> Unit,
    navigateToBack: (String) -> Unit,
    navigateToHome: () -> Unit,
    viewModel: PasswordEnterViewModel = hiltViewModel<PasswordEnterViewModel>(),
) {
    BackHandler {
        navigateToBack(viewModel.email)
    }

    val state = viewModel.authByPasswordState.value
    when (state) {
        is AuthByPasswordState.Error -> onError(state.exception)
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 40.dp,
                            end = 40.dp,
                        ),
                ) {
                    IconButton(
                        onClick = remember {
                            {
                                navigateToBack(viewModel.email)
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MejourneyTheme.colors.graphicPrimary,
                            modifier = Modifier
                                .size(36.dp),
                        )
                    }
                    Text(
                        text = stringResource(R.string.password_enter_title),
                        style = MejourneyTheme.typography.displaySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f),
                    )
                }

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

            ContinueButton(
                onClick = remember {
                    {
                        viewModel.validatePassword()
                    }
                },
                text = stringResource(R.string.password_enter_continue_button),
                needToBlockUi = state.needToBlockUi,
            )
        }

        if (state.needToBlockUi) {
            BlockingSurface()
        }
    }
}