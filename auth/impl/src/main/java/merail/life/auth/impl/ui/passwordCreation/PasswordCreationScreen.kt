package merail.life.auth.impl.ui.passwordCreation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import merail.life.auth.impl.ui.passwordCreation.state.UserCreatingState
import merail.life.auth.impl.ui.passwordCreation.state.needToBlockUi
import merail.life.design.MejourneyTheme
import merail.life.design.components.BlockingSurface
import merail.life.design.components.ContinueButton

@Composable
fun PasswordCreationContainer(
    onError: (Throwable?) -> Unit,
    navigateToHome: () -> Unit,
) = PasswordCreationScreen(
    onError = onError,
    navigateToHome = navigateToHome,
)

@Composable
internal fun PasswordCreationScreen(
    onError: (Throwable?) -> Unit,
    navigateToHome: () -> Unit,
    viewModel: PasswordCreationViewModel = hiltViewModel<PasswordCreationViewModel>(),
) {
    val state = viewModel.userCreatingState.value

    when (state) {
        is UserCreatingState.Error -> onError(state.exception)
        is UserCreatingState.Success -> LaunchedEffect(null) {
            navigateToHome()
        }
        is UserCreatingState.None,
        is UserCreatingState.Loading,
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
                    text = stringResource(R.string.password_creation_title),
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
                    text = stringResource(R.string.password_creation_description),
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
                    imeAction = ImeAction.Next,
                    label = stringResource(R.string.password_creation_label),
                    errorText = stringResource(R.string.password_creation_validation_error),
                )

                if (viewModel.passwordValueState.value.isNotEmpty()) {
                    PasswordField(
                        passwordValueState = viewModel.repeatedPasswordValueState,
                        onChange = remember {
                            {
                                viewModel.updateRepeatedPassword(it)
                            }
                        },
                        imeAction = ImeAction.Done,
                        label = stringResource(R.string.password_creation_repeated_label),
                        errorText = stringResource(R.string.password_creation_repeated_validation_error),
                    )
                }
            }

            ContinueButton(
                onClick = remember {
                    {
                        viewModel.validate()
                    }
                },
                needToBlockUi = state.needToBlockUi,
                text = stringResource(R.string.password_creation_continue_button),
            )
        }

        if (state.needToBlockUi) {
            BlockingSurface()
        }
    }
}
