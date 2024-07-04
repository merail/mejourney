package merail.life.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import merail.life.core.NavigationDestination
import merail.life.core.extensions.activity
import merail.life.design.MejourneyTheme
import merail.life.design.components.ErrorMessage
import merail.life.design.components.Loading

object AuthDestination : NavigationDestination {
    override val route = "auth"
}

@Composable
fun AuthScreen(
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
            is AuthUiState.Error -> ErrorMessage(uiState.exception.message.orEmpty())
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

@Preview
@Composable
private fun PhoneEnter(
    phoneNumber: String = "",
    onPhoneNumberChange: (String) -> Unit = {},
    onSendClick: () -> Unit = {},
) {
    MejourneyTheme {
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
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = Color.Black,
                ),
                modifier = Modifier
            ) {
                Text(
                    text = "Send phone number",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SmsCodeEnter(
    smsCode: String = "",
    onSmsCodeChange: (String) -> Unit = {},
    onSendClick: () -> Unit = {},
) {
    MejourneyTheme {
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
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = Color.Black,
                ),
                modifier = Modifier
            ) {
                Text(
                    text = "Send sms-code",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}