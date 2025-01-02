package merail.life.auth.impl.ui.otpInput

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import merail.life.auth.impl.R
import merail.life.auth.impl.ui.otpInput.state.needToBlockUi
import merail.life.core.extensions.toCountdownTime
import merail.life.design.MejourneyTheme

@Composable
fun OtpInputContainer(
    navigateToBack: (String) -> Unit,
    navigateToPassword: (String) -> Unit,
) = OtpInputScreen(
    navigateToBack = navigateToBack,
    navigateToPassword = navigateToPassword,
)

@Composable
internal fun OtpInputScreen(
    navigateToBack: (String) -> Unit,
    navigateToPassword: (String) -> Unit,
    viewModel: OtpInputViewModel = hiltViewModel<OtpInputViewModel>(),
) {
    BackHandler {
        navigateToBack(viewModel.email)
    }

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
                    text = stringResource(R.string.otp_input_title),
                    style = MejourneyTheme.typography.displaySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f),
                )
            }

            Text(
                text = stringResource(R.string.otp_input_description),
                style = MejourneyTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 20.dp,
                    ),
            )

            OtpField(
                viewModel = viewModel,
                onOtpTextChange = remember {
                    {
                        viewModel.updateOtp(it)
                        if (it.length == 4) {
                            if (viewModel.verifyOtp()) {
                                navigateToPassword(viewModel.email)
                            }
                        }
                    }
                },
            )
        }
    }
}

@Composable
private fun OtpField(
    viewModel: OtpInputViewModel,
    onOtpTextChange: (String) -> Unit,
) {
    val otpValueState = viewModel.otpValueState
    val focusRequester = remember { FocusRequester() }
    Column(
        modifier = Modifier
            .padding(12.dp),
    ) {
        BasicTextField(
            value = otpValueState.value,
            onValueChange = remember {
                {
                    if (viewModel.isInputAvailable) {
                        if (it.length <= 4) {
                            onOtpTextChange(it)
                        }
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
            ),
            decorationBox = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                ) {
                    repeat(4) { index ->
                        OtpCell(
                            index = index,
                            text = otpValueState.value,
                            isError = otpValueState.isOtpVerified.not(),
                            modifier = Modifier
                                .weight(1f),
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
        )

        if (viewModel.isValid.not()) {
            Text(
                text = stringResource(
                    when {
                        otpValueState.hasAvailableAttempts.not() -> R.string.otp_input_block_error
                        otpValueState.isOtpNotExpired.not() -> R.string.otp_input_expired_error
                        else -> R.string.otp_input_validation_error
                    },
                ),
                style = MejourneyTheme.typography.bodyMedium,
                color = MejourneyTheme.colors.textNegative,
                modifier = Modifier
                    .padding(
                        start = 12.dp,
                        top = 8.dp,
                        end = 12.dp,
                    ),
            )
        }

        if (viewModel.isInputAvailable) {
            Text(
                text = stringResource(
                    R.string.otp_input_email_hint,
                    viewModel.email,
                ),
                style = MejourneyTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(
                        start = 12.dp,
                        top = 8.dp,
                        end = 12.dp,
                    ),
            )
        }

        Row(
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    top = 4.dp,
                    end = 12.dp,
                ),
        ) {
            Text(
                text = stringResource(R.string.otp_input_resend_countdown_hint_text_part),
                style = MejourneyTheme.typography.bodyMedium,
                textDecoration = if (viewModel.isCountdownTextVisible) {
                    TextDecoration.None
                } else {
                    TextDecoration.Underline
                },
                modifier = Modifier.then(
                    if (viewModel.isCountdownTextVisible.not() && viewModel.otpResendState.needToBlockUi.not()) {
                        Modifier.clickable {
                            viewModel.resendOtp()
                        }
                    } else {
                        Modifier
                    }
                ),
            )

            if (viewModel.isCountdownTextVisible) {
                Text(
                    text = stringResource(
                        R.string.otp_input_resend_countdown_hint_seconds_part,
                        viewModel.otpResendRemindTime.toCountdownTime(),
                    ),
                    style = MejourneyTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(
                            start = 4.dp,
                        ),
                )
            }

            if (viewModel.otpResendState.needToBlockUi) {
                CircularProgressIndicator(
                    strokeWidth = 3.dp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(
                            start = 4.dp,
                        )
                        .size(16.dp),
                )
            }
        }
    }

    LaunchedEffect(null) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun OtpCell(
    index: Int,
    text: String,
    isError: Boolean,
    modifier: Modifier,
) {
    val isCursorVisible = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(isCursorVisible.value) {
        delay(500)
        isCursorVisible.value = isCursorVisible.value.not()
    }
    val isFocused = text.length == index
    val char = if (index >= text.length) {
        ""
    } else {
        text[index].toString()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .requiredSize(72.dp)
            .border(
                width = 1.dp,
                color = if (isError) {
                    MejourneyTheme.colors.graphicNegativePrimary
                } else {
                    MejourneyTheme.colors.graphicPrimary
                },
                shape = RoundedCornerShape(8.dp),
            )
            .padding(2.dp),
    ) {
        if (isFocused) {
            AnimatedVisibility(
                visible = isCursorVisible.value,
                enter = fadeIn(
                    animationSpec = TweenSpec(
                        durationMillis = 500,
                    ),
                ),
                exit = fadeOut(
                    animationSpec = TweenSpec(
                        durationMillis = 500,
                    ),
                ),
            ) {
                VerticalDivider(
                    thickness = 2.5.dp,
                    color = MejourneyTheme.colors.graphicPrimary,
                    modifier = Modifier
                        .padding(
                            vertical = 16.dp,
                        ),
                )
            }
        } else {
            Text(
                text = char,
                style = MejourneyTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
                color = if (isError) {
                    MejourneyTheme.colors.textNegative
                } else {
                    MejourneyTheme.colors.textPrimary
                }
            )
        }
    }
}