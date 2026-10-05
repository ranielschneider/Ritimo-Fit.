package com.ranielschneider.ritimofit.presentation.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.ranielschneider.ritimofit.core.design.background.RitimoAmbientBackground
import com.ranielschneider.ritimofit.core.design.components.button.RitimoIconButton
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme
import com.ranielschneider.ritimofit.core.design.theme.ThemeMode
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthContainerScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = koinViewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()

    RitimoTheme(themeMode = themeMode) {
        RitimoAmbientBackground(
            modifier = modifier.fillMaxSize()
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding(),
                topBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .safeContentPadding()
                            .padding(horizontal = RitimoTheme.spacing.md, vertical = RitimoTheme.spacing.xs),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (currentScreen != AuthSubScreen.LOGIN) {
                            RitimoIconButton(
                                icon = RitimoIcons.ArrowBack,
                                contentDescription = "Voltar",
                                onClick = { viewModel.navigateBack() }
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Box(
                            modifier = Modifier
                                .clip(RitimoTheme.shapes.pill)
                                .background(RitimoTheme.colors.surfaceVariant.copy(alpha = 0.5f))
                                .clickable { viewModel.toggleThemeMode() }
                                .padding(horizontal = RitimoTheme.spacing.sm, vertical = RitimoTheme.spacing.xxs)
                        ) {
                            Text(
                                text = if (themeMode == ThemeMode.DARK) "Modo Claro" else "Modo Escuro",
                                style = RitimoTheme.typography.labelSmall,
                                color = RitimoTheme.colors.onSurfaceVariant
                            )
                        }
                    }
                },
                containerColor = Color.Transparent
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    AnimatedContent(
                        targetState = currentScreen,
                        transitionSpec = { fadeIn() togetherWith fadeOut() }
                    ) { screen ->
                        when (screen) {
                            AuthSubScreen.LOGIN -> LoginScreen(viewModel = viewModel)
                            AuthSubScreen.FORGOT_PASSWORD -> ForgotPasswordScreen(viewModel = viewModel)
                            AuthSubScreen.ROLE_SELECTION -> RoleSelectionScreen(viewModel = viewModel)
                            AuthSubScreen.REGISTER_INITIAL -> RegisterInitialScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
