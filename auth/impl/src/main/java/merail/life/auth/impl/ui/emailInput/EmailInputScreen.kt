package merail.life.auth.impl.ui.emailInput

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import merail.life.auth.impl.R
import merail.life.auth.impl.ui.emailInput.state.EmailAuthState
import merail.life.auth.impl.ui.emailInput.state.EmailState
import merail.life.auth.impl.ui.emailInput.state.needToBlockUi
import merail.life.core.NavigationDestination
import merail.life.design.MejourneyTheme
import merail.life.design.styles.ButtonStyle
import merail.life.design.styles.TextFieldStyle

object EmailInputDestination : NavigationDestination {
    override val route = "emailInput"
}

@Composable
fun EmailInputScreen(
    onError: (Throwable?) -> Unit,
    navigateToPassword: (String) -> Unit,
    navigateToOtp: (String) -> Unit,
    viewModel: EmailInputViewModel = hiltViewModel<EmailInputViewModel>(),
) {
    val state = viewModel.emailAuthState.value
    when (state) {
        is EmailAuthState.Error -> onError(state.exception)
        is EmailAuthState.UserExists -> navigateToPassword(viewModel.emailState.value)
        is EmailAuthState.OtpWasSent -> navigateToOtp(viewModel.emailState.value)
        is EmailAuthState.None,
        is EmailAuthState.Loading,
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
                    text = stringResource(R.string.email_input_title),
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
                    text = stringResource(R.string.email_input_description),
                    style = MejourneyTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 24.dp,
                            vertical = 20.dp,
                        ),
                )

                EmailField(
                    emailState = viewModel.emailState,
                    onChange = viewModel::updateEmail,
                )
            }

            Button(
                onClick = viewModel::validateEmail,
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
                        text = stringResource(R.string.email_input_continue_button),
                        textAlign = TextAlign.Center,
                        style = MejourneyTheme.typography.titleMedium,
                    )
                }
            }
        }
        if (state.needToBlockUi) {
            Surface(
                color = Color.Transparent,
                modifier = Modifier
                    .fillMaxSize(),
            ) {}
        }
    }
}

@Composable
private fun EmailField(
    emailState: EmailState,
    onChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(12.dp),
    ) {
        TextField(
            value = emailState.value,
            onValueChange = onChange,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "",
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Email,
            ),
            label = {
                Text(
                    text = stringResource(R.string.email_input_label),
                )
            },
            colors = TextFieldStyle.Primary.colors(),
            singleLine = true,
            isError = emailState.isValid.not(),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
        )
        if (emailState.isValid.not()) {
            Text(
                text = stringResource(R.string.email_input_validation_error),
                color = MejourneyTheme.colors.textNegative,
                modifier = Modifier
                    .padding(
                        start = 12.dp,
                        top = 4.dp,
                    ),
            )
        }
    }
}