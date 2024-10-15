package merail.life.auth.impl.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import merail.life.auth.impl.ui.passwordCreation.state.PasswordValueState
import merail.life.design.MejourneyTheme
import merail.life.design.styles.TextFieldStyle

@Composable
internal fun PasswordField(
    passwordValueState: PasswordValueState,
    onChange: (String) -> Unit,
    imeAction: ImeAction,
    label: String,
    errorText: String,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp,
            ),
    ) {
        TextField(
            value = passwordValueState.value,
            onValueChange = onChange,
            leadingIcon = {
                Icon(
                    Icons.Default.Key,
                    contentDescription = "",
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = remember {
                        {
                            isPasswordVisible = !isPasswordVisible
                        }
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
            isError = passwordValueState.isValid.not(),
            visualTransformation = if (isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
        )
        if (passwordValueState.isValid.not()) {
            Text(
                text = errorText,
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