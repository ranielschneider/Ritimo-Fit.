package com.ranielschneider.ritimofit.presentation.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.ranielschneider.ritimofit.core.design.components.button.RitimoSecondaryButton
import com.ranielschneider.ritimofit.core.design.components.card.RitimoGlassCard
import com.ranielschneider.ritimofit.core.design.components.field.RitimoTextField
import com.ranielschneider.ritimofit.core.design.components.state.RitimoSuccessState
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme
import com.ranielschneider.ritimofit.presentation.base.UiState

@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val email by viewModel.forgotEmail.collectAsState()
    val emailError by viewModel.forgotEmailError.collectAsState()
    val forgotUiState by viewModel.forgotUiState.collectAsState()

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (forgotUiState is UiState.Success) {
            RitimoSuccessState(
                title = "Instruções enviadas",
                message = "Se o e-mail $email estiver cadastrado, você receberá um link para redefinir sua senha.",
                actionText = "Voltar ao Login",
                onAction = {
                    viewModel.resetForgotState()
                    viewModel.navigateTo(AuthSubScreen.LOGIN)
                }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = RitimoTheme.spacing.lg, vertical = RitimoTheme.spacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                Text(
                    text = "Recuperar senha",
                    style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                    color = RitimoTheme.colors.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

                Text(
                    text = "Informe seu e-mail de cadastro para enviarmos as instruções de redefinição",
                    style = RitimoTheme.typography.bodyMedium,
                    color = RitimoTheme.colors.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

                RitimoGlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        RitimoTextField(
                            value = email,
                            onValueChange = viewModel::onForgotEmailChanged,
                            label = "E-mail",
                            placeholder = "seu@email.com",
                            errorMessage = emailError,
                            leadingIcon = RitimoIcons.Email,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    viewModel.submitForgotPassword()
                                }
                            )
                        )

                        if (forgotUiState is UiState.Error) {
                            val errorMsg = (forgotUiState as UiState.Error).message ?: "E-mail não encontrado."
                            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
                            Text(
                                text = errorMsg,
                                style = RitimoTheme.typography.bodySmall,
                                color = RitimoTheme.colors.error
                            )
                        }

                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                        RitimoPrimaryButton(
                            text = "Enviar instruções",
                            onClick = {
                                focusManager.clearFocus()
                                viewModel.submitForgotPassword()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            isLoading = forgotUiState is UiState.Loading
                        )

                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.sm))

                        RitimoSecondaryButton(
                            text = "Voltar",
                            onClick = { viewModel.navigateTo(AuthSubScreen.LOGIN) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
