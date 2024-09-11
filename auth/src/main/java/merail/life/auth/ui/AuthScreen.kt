package merail.life.auth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import merail.life.core.NavigationDestination
import merail.life.design.MejourneyTheme
import merail.life.design.styles.ButtonStyle
import merail.life.design.styles.TextFieldStyle

object AuthDestination : NavigationDestination {
    override val route = "auth"
}

@Composable
fun AuthScreen(
    onError: (Throwable?) -> Unit,
    navigateToHome: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel<AuthViewModel>(),
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
                text = "Регистрация",
                style = MejourneyTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
            )

            LoginField(
                value = viewModel.email,
                onChange = {
                    viewModel.updateEmail(it)
                },
            )

            PasswordField(
                value = viewModel.password,
                onChange = {
                    viewModel.updatePassword(it)
                },
                imeAction = ImeAction.Next,
                label = "Введи пароль",
            )

            if (viewModel.password.isNotEmpty()) {
                PasswordField(
                    value = viewModel.repeatedPassword,
                    onChange = {
                        viewModel.updateRepeatedPassword(it)
                    },
                    imeAction = ImeAction.Done,
                    label = "Введи пароль еще раз",
                )
            }
        }

        Button(
            onClick = {
                viewModel.createUser()
            },
            colors = ButtonStyle.Primary.colors(),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .height(64.dp),
        ) {
            Text(
                text = "Создать",
                textAlign = TextAlign.Center,
                style = MejourneyTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun LoginField(
    value: String,
    onChange: (String) -> Unit,
) {

    val focusManager = LocalFocusManager.current

    TextField(
        value = value,
        onValueChange = onChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "",
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email,
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            },
        ),
        label = {
            Text(
                text = "Введи email",
            )
        },
        colors = TextFieldStyle.Primary.colors(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp,
            )
            .fillMaxWidth()
            .height(64.dp),
    )
}

@Composable
private fun PasswordField(
    value: String,
    onChange: (String) -> Unit,
    imeAction: ImeAction,
    label: String,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onChange,
        leadingIcon = {
            Icon(
                Icons.Default.Key,
                contentDescription = "",
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    isPasswordVisible = !isPasswordVisible
                },
            ) {
                Icon(
                    imageVector = if (isPasswordVisible) {
                        Icons.Default.VisibilityOff
                    } else {
                        Icons.Default.Visibility
                    },
                    contentDescription = "",
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = KeyboardType.Password,
        ),
        label = {
            Text(
                text = label,
            )
        },
        colors = TextFieldStyle.Primary.colors(),
        singleLine = true,
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp,
            )
            .fillMaxWidth()
            .height(64.dp),
    )
}