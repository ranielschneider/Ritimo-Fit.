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
import com.ranielschneider.ritimofit.core.design.components.field.RitimoPasswordField
import com.ranielschneider.ritimofit.core.design.components.field.RitimoTextField
import com.ranielschneider.ritimofit.core.design.components.indicator.BadgeType
import com.ranielschneider.ritimofit.core.design.components.indicator.RitimoStatusBadge
import com.ranielschneider.ritimofit.core.design.components.state.RitimoSuccessState
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme
import com.ranielschneider.ritimofit.domain.model.UserRole
import com.ranielschneider.ritimofit.presentation.base.UiState

@Composable
fun RegisterInitialScreen(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val selectedRole by viewModel.selectedRole.collectAsState()
    val name by viewModel.registerName.collectAsState()
    val email by viewModel.registerEmail.collectAsState()
    val password by viewModel.registerPassword.collectAsState()
    val confirmPassword by viewModel.registerConfirmPassword.collectAsState()

    val nameError by viewModel.registerNameError.collectAsState()
    val emailError by viewModel.registerEmailError.collectAsState()
    val passwordError by viewModel.registerPasswordError.collectAsState()
    val confirmError by viewModel.registerConfirmError.collectAsState()
    val registerUiState by viewModel.registerUiState.collectAsState()

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val roleTitle = if (selectedRole == UserRole.PROFESSOR) "Cadastro de Professor" else "Cadastro de Aluno"

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (registerUiState is UiState.Success) {
            val user = (registerUiState as UiState.Success).data
            RitimoSuccessState(
                title = "Conta criada com sucesso!",
                message = "Bem-vindo, ${user.fullName}! Sua conta de ${if (user.role == UserRole.PROFESSOR) "Professor" else "Aluno"} foi inicializada.",
                actionText = "Ir para o aplicativo",
                onAction = {
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

                RitimoStatusBadge(
                    text = if (selectedRole == UserRole.PROFESSOR) "PERFIL PROFESSOR" else "PERFIL ALUNO",
                    type = BadgeType.PRIMARY
                )

                Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

                Text(
                    text = roleTitle,
                    style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                    color = RitimoTheme.colors.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

                Text(
                    text = "Preencha seus dados para criar sua conta no Ritimo Fit",
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
                            value = name,
                            onValueChange = viewModel::onRegisterNameChanged,
                            label = "Nome completo",
                            placeholder = "Seu nome",
                            errorMessage = nameError,
                            leadingIcon = RitimoIcons.User,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            )
                        )

                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

                        RitimoTextField(
                            value = email,
                            onValueChange = viewModel::onRegisterEmailChanged,
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
                            onValueChange = viewModel::onRegisterPasswordChanged,
                            label = "Senha",
                            placeholder = "Mínimo 6 caracteres",
                            errorMessage = passwordError,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Next
                            )
                        )

                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

                        RitimoPasswordField(
                            value = confirmPassword,
                            onValueChange = viewModel::onRegisterConfirmPasswordChanged,
                            label = "Confirmar senha",
                            placeholder = "Repita sua senha",
                            errorMessage = confirmError,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    viewModel.submitRegister()
                                }
                            )
                        )

                        if (registerUiState is UiState.Error) {
                            val errorMsg = (registerUiState as UiState.Error).message ?: "Erro ao realizar cadastro"
                            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
                            Text(
                                text = errorMsg,
                                style = RitimoTheme.typography.bodySmall,
                                color = RitimoTheme.colors.error
                            )
                        }

                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                        RitimoPrimaryButton(
                            text = "Criar minha conta",
                            onClick = {
                                focusManager.clearFocus()
                                viewModel.submitRegister()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            isLoading = registerUiState is UiState.Loading
                        )

                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.sm))

                        RitimoSecondaryButton(
                            text = "Voltar",
                            onClick = { viewModel.navigateTo(AuthSubScreen.ROLE_SELECTION) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
