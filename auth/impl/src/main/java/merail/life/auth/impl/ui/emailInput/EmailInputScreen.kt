package merail.life.auth.impl.ui.emailInput

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import merail.life.auth.impl.R
import merail.life.auth.impl.ui.emailInput.state.EmailAuthState
import merail.life.auth.impl.ui.emailInput.state.EmailValueState
import merail.life.auth.impl.ui.emailInput.state.needToBlockUi
import merail.life.design.MejourneyTheme
import merail.life.design.components.BlockingSurface
import merail.life.design.components.ContinueButton
import merail.life.design.styles.TextFieldStyle

@Composable
fun EmailInputContainer(
    onError: (Throwable?) -> Unit,
    navigateToPasswordEnter: (String) -> Unit,
    navigateToOtp: (String) -> Unit,
) = EmailInputScreen(
    onError = onError,
    navigateToPasswordEnter = navigateToPasswordEnter,
    navigateToOtp = navigateToOtp,
)

@Composable
internal fun EmailInputScreen(
    onError: (Throwable?) -> Unit,
    navigateToPasswordEnter: (String) -> Unit,
    navigateToOtp: (String) -> Unit,
    viewModel: EmailInputViewModel = hiltViewModel<EmailInputViewModel>(),
) {
    val state = viewModel.emailAuthState
    when (state) {
        is EmailAuthState.Error -> LaunchedEffect(null) {
            onError(state.exception)
        }
        is EmailAuthState.UserExists -> LaunchedEffect(null) {
            navigateToPasswordEnter(viewModel.emailValueState.value)
        }
        is EmailAuthState.OtpWasSent -> LaunchedEffect(null) {
            navigateToOtp(viewModel.emailValueState.value)
        }
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
                    emailValueState = viewModel.emailValueState,
                    onChange = remember {
                        {
                            viewModel.updateEmail(it)
                        }
                    },
                )
            }

            ContinueButton(
                onClick = remember {
                    {
                        viewModel.validateEmail()
                    }
                },
                text = stringResource(R.string.email_input_continue_button),
                needToBlockUi = state.needToBlockUi,
            )
        }

        if (state.needToBlockUi) {
            BlockingSurface()
        }
    }
}

@Composable
private fun EmailField(
    emailValueState: EmailValueState,
    onChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(12.dp),
    ) {
        TextField(
            value = emailValueState.value,
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
            isError = emailValueState.isValid.not(),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
        )
        if (emailValueState.isValid.not()) {
            Text(
                text = stringResource(R.string.email_input_validation_error),
                style = MejourneyTheme.typography.bodyMedium,
                color = MejourneyTheme.colors.textNegative,
                modifier = Modifier
                    .padding(
                        start = 12.dp,
                        top = 8.dp,
                    ),
            )
        }

        Text(
            text = buildPersonalDataConsentText(),
            style = MejourneyTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    top = 8.dp,
                    end = 12.dp,
                ),
        )
    }
}

@Composable
private fun buildPersonalDataConsentText() = buildAnnotatedString {
    withStyle(
        style = MejourneyTheme.typography.bodyMedium.toSpanStyle(),
    ) {
        append(
            text = stringResource(R.string.email_input_personal_data_consent_text_hint),
        )
    }

    withLink(
        link = LinkAnnotation.Url(
            url = stringResource(R.string.email_input_personal_data_consent_link_value),
        ),
    ) {
        withStyle(
            style = MejourneyTheme.typography.bodyMedium.copy(
                textDecoration = TextDecoration.Underline,
            ).toSpanStyle(),
        ) {
            append(
                text = stringResource(R.string.email_input_personal_data_consent_link_hint),
            )
        }
    }
}