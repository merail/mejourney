package merail.life.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import merail.life.core.NavigationDestination
import merail.life.core.extensions.activity
import merail.life.design.MejourneyTheme
import merail.life.design.components.Loading

object AuthDestination : NavigationDestination {
    override val route = "auth"
}

@Composable
fun AuthScreen(
    onError: (Throwable?) -> Unit,
    navigateToHome: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel<AuthViewModel>(),
) {
    LocalContext.current.activity?.run {
        when (val uiState = viewModel.uiState.collectAsState().value) {
            is AuthUiState.PhoneEnter -> PhoneEnter(
                phoneNumber = viewModel.phoneNumber,
                onPhoneNumberChange = viewModel::updatePhoneNumber,
                onSendClick = {
                    viewModel.sendCode(this)
                },
            )
            is AuthUiState.Loading -> Loading()
            is AuthUiState.Error -> LaunchedEffect(null) {
                onError(uiState.exception)
            }
            is AuthUiState.SmsEnter -> SmsCodeEnter(
                smsCode = viewModel.smsCode,
                onSmsCodeChange = viewModel::updateSmsCode,
                onSendClick = {
                    viewModel.auth(this)
                },
            )
            is AuthUiState.Success -> navigateToHome()
        }
    }
}

@Composable
private fun PhoneEnter(
    phoneNumber: String = "",
    onPhoneNumberChange: (String) -> Unit = {},
    onSendClick: () -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        TextField(
            value = phoneNumber,
            onValueChange = {
                onPhoneNumberChange(it)
            },
            modifier = Modifier
                .wrapContentHeight()
                .weight(1f),
        )

        Button(
            onClick = {
                onSendClick()
            },
            modifier = Modifier
        ) {
            Text(
                text = "Send phone number",
                style = MejourneyTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun SmsCodeEnter(
    smsCode: String = "",
    onSmsCodeChange: (String) -> Unit = {},
    onSendClick: () -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        TextField(
            value = smsCode,
            onValueChange = {
                onSmsCodeChange(it)
            },
            modifier = Modifier
                .wrapContentHeight()
                .weight(1f),
        )

        Button(
            onClick = {
                onSendClick()
            },
            modifier = Modifier
        ) {
            Text(
                text = "Send sms-code",
                style = MejourneyTheme.typography.bodyLarge,
            )
        }
    }
}