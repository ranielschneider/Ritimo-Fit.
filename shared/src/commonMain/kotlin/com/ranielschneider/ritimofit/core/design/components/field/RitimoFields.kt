package com.ranielschneider.ritimofit.core.design.components.field

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme

@Composable
fun RitimoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    errorMessage: String? = null,
    isError: Boolean = errorMessage != null,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            isError = isError,
            label = if (label.isNotEmpty()) { { Text(text = label, style = RitimoTheme.typography.bodyMedium) } } else null,
            placeholder = placeholder?.let { { Text(text = it, style = RitimoTheme.typography.bodyMedium) } },
            singleLine = singleLine,
            shape = RitimoTheme.shapes.medium,
            textStyle = RitimoTheme.typography.bodyLarge.copy(color = RitimoTheme.colors.onSurface),
            visualTransformation = visualTransformation,
            leadingIcon = leadingIcon?.let {
                {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = if (isError) RitimoTheme.colors.error else RitimoTheme.colors.onSurfaceVariant
                    )
                }
            },
            trailingIcon = trailingIcon,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = RitimoTheme.colors.onSurface,
                unfocusedTextColor = RitimoTheme.colors.onSurface,
                disabledTextColor = RitimoTheme.colors.onSurfaceVariant,
                errorTextColor = RitimoTheme.colors.onSurface,
                focusedContainerColor = RitimoTheme.colors.surfaceVariant.copy(alpha = 0.6f),
                unfocusedContainerColor = RitimoTheme.colors.surface,
                disabledContainerColor = RitimoTheme.colors.surfaceVariant.copy(alpha = 0.2f),
                focusedBorderColor = RitimoTheme.colors.primary,
                unfocusedBorderColor = RitimoTheme.colors.outline,
                errorBorderColor = RitimoTheme.colors.error,
                focusedLabelColor = RitimoTheme.colors.primary,
                unfocusedLabelColor = RitimoTheme.colors.onSurfaceVariant,
                errorLabelColor = RitimoTheme.colors.error,
                focusedPlaceholderColor = RitimoTheme.colors.onSurfaceVariant.copy(alpha = 0.7f),
                unfocusedPlaceholderColor = RitimoTheme.colors.onSurfaceVariant.copy(alpha = 0.7f),
                cursorColor = RitimoTheme.colors.primary
            )
        )
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = RitimoTheme.colors.error,
                style = RitimoTheme.typography.bodySmall,
                modifier = Modifier.padding(start = RitimoTheme.spacing.xs, top = RitimoTheme.spacing.xxs)
            )
        }
    }
}

@Composable
fun RitimoPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    errorMessage: String? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    var passwordVisible by remember { mutableStateOf(false) }

    RitimoTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        placeholder = placeholder,
        errorMessage = errorMessage,
        enabled = enabled,
        leadingIcon = RitimoIcons.Lock,
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) RitimoIcons.EyeOff else RitimoIcons.Eye,
                    contentDescription = if (passwordVisible) "Ocultar senha" else "Mostrar senha",
                    tint = RitimoTheme.colors.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    )
}

@Composable
fun RitimoSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String = "Buscar...",
    modifier: Modifier = Modifier,
    onClear: () -> Unit = { onQueryChange("") }
) {
    RitimoTextField(
        value = query,
        onValueChange = onQueryChange,
        label = "",
        placeholder = placeholder,
        modifier = modifier,
        leadingIcon = RitimoIcons.Search,
        singleLine = true,
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(
                        imageVector = RitimoIcons.Clear,
                        contentDescription = "Limpar busca",
                        tint = RitimoTheme.colors.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    )
}
