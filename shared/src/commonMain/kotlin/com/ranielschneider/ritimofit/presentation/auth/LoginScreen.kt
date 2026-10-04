package com.ranielschneider.ritimofit.presentation.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.ranielschneider.ritimofit.core.design.components.button.RitimoPrimaryButton
import com.ranielschneider.ritimofit.core.design.components.button.RitimoTertiaryButton
import com.ranielschneider.ritimofit.core.design.components.card.RitimoGlassCard
import com.ranielschneider.ritimofit.core.design.components.field.RitimoPasswordField
import com.ranielschneider.ritimofit.core.design.components.field.RitimoTextField
import com.ranielschneider.ritimofit.core.design.components.indicator.BadgeType
import com.ranielschneider.ritimofit.core.design.components.indicator.RitimoStatusBadge
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme
import com.ranielschneider.ritimofit.presentation.base.UiState

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val email by viewModel.loginEmail.collectAsState()
    val password by viewModel.loginPassword.collectAsState()
    val emailError by viewModel.loginEmailError.collectAsState()
    val passwordError by viewModel.loginPasswordError.collectAsState()
    val loginUiState by viewModel.loginUiState.collectAsState()

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = RitimoTheme.spacing.lg, vertical = RitimoTheme.spacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

            RitimoStatusBadge(
                text = "RITIMO FIT • PERFORMANCE",
                type = BadgeType.PRIMARY
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

            Text(
                text = "Bem-vindo de volta",
                style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                color = RitimoTheme.colors.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

            Text(
                text = "Acesse sua conta para acompanhar seu treino e dieta",
                style = RitimoTheme.typography.bodyMedium,
                color = RitimoTheme.colors.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

            RitimoGlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RitimoTextField(
                        value = email,
                        onValueChange = viewModel::onLoginEmailChanged,
                        label = "E-mail",
                        placeholder = "seu@email.com",
                        errorMessage = emailError,
                        leadingIcon = RitimoIcons.Email,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

                    RitimoPasswordField(
                        value = password,
                        onValueChange = viewModel::onLoginPasswordChanged,
                        label = "Senha",
                        placeholder = "Sua senha",
                        errorMessage = passwordError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                viewModel.submitLogin()
                            }
                        )
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        RitimoTertiaryButton(
                            text = "Esqueci minha senha",
                            onClick = { viewModel.navigateTo(AuthSubScreen.FORGOT_PASSWORD) }
                        )
                    }

                    if (loginUiState is UiState.Error) {
                        val errorMsg = (loginUiState as UiState.Error).message ?: "Falha na autenticação"
                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
                        Text(
                            text = errorMsg,
                            style = RitimoTheme.typography.bodySmall,
                            color = RitimoTheme.colors.error
                        )
                    }

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

                    RitimoPrimaryButton(
                        text = "Entrar",
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.submitLogin()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isLoading = loginUiState is UiState.Loading
                    )
                }
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Não tem uma conta?",
                    style = RitimoTheme.typography.bodyMedium,
                    color = RitimoTheme.colors.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(RitimoTheme.spacing.xxs))
                RitimoTertiaryButton(
                    text = "Criar conta",
                    onClick = { viewModel.navigateTo(AuthSubScreen.ROLE_SELECTION) }
                )
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))
        }
    }
}
