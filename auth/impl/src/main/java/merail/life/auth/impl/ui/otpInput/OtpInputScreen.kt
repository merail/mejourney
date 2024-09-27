package merail.life.auth.impl.ui.otpInput

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import merail.life.auth.impl.R
import merail.life.core.NavigationDestination
import merail.life.design.MejourneyTheme

object OtpInputDestination : NavigationDestination {
    override val route = "otpInput"
}

@Composable
fun OtpInputScreen(
    onError: (Throwable?) -> Unit,
    navigateToPassword: () -> Unit,
    viewModel: OtpInputViewModel = hiltViewModel<OtpInputViewModel>(),
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
                text = stringResource(R.string.otp_input_title),
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
                otpText = viewModel.otpState.value,
                onOtpTextChange = {
                    viewModel.updateOtp(it)
                    if (it.length == 4) {
                        navigateToPassword()
                    }
                },
            )
        }
    }
}

@Composable
private fun OtpField(
    otpText: String,
    onOtpTextChange: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    BasicTextField(
        value = otpText,
        onValueChange = {
            if (it.length <= 4) {
                onOtpTextChange(it)
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
                        text = otpText,
                    )
                }
            }
        },
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester),
    )
    LaunchedEffect(null) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun OtpCell(
    index: Int,
    text: String,
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
        modifier = Modifier
            .padding(
                start = if (index == 0) {
                    0.dp
                } else {
                    12.dp
                },
                end = if (index == 4) {
                    0.dp
                } else {
                    12.dp
                },
            )
            .size(72.dp)
            .border(
                width = 1.dp,
                color = MejourneyTheme.colors.graphicPrimary,
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
            )
        }
    }
}