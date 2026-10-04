package com.ranielschneider.ritimofit.presentation.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ranielschneider.ritimofit.core.design.components.button.RitimoPrimaryButton
import com.ranielschneider.ritimofit.core.design.components.button.RitimoSecondaryButton
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme
import com.ranielschneider.ritimofit.domain.model.UserRole

@Composable
fun RoleSelectionScreen(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val selectedRole by viewModel.selectedRole.collectAsState()
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

            Text(
                text = "Como você deseja usar o Ritimo Fit?",
                style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                color = RitimoTheme.colors.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

            Text(
                text = "Escolha seu perfil para personalizar sua experiência no aplicativo",
                style = RitimoTheme.typography.bodyMedium,
                color = RitimoTheme.colors.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

            RoleOptionCard(
                title = "Sou Professor / Personal",
                description = "Prescreva treinos, dietas, acompanhe a evolução dos seus alunos e gerencie sua consultoria.",
                icon = RitimoIcons.User,
                isSelected = selectedRole == UserRole.PROFESSOR,
                onClick = { viewModel.onRoleSelected(UserRole.PROFESSOR) }
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

            RoleOptionCard(
                title = "Sou Aluno",
                description = "Acesse seus treinos, acompanhe sua dieta, consulte demonstrações e registre seu progresso.",
                icon = RitimoIcons.User,
                isSelected = selectedRole == UserRole.STUDENT,
                onClick = { viewModel.onRoleSelected(UserRole.STUDENT) }
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

            RitimoPrimaryButton(
                text = "Continuar",
                onClick = { viewModel.navigateTo(AuthSubScreen.REGISTER_INITIAL) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.sm))

            RitimoSecondaryButton(
                text = "Voltar ao Login",
                onClick = { viewModel.navigateTo(AuthSubScreen.LOGIN) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun RoleOptionCard(
    title: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) RitimoTheme.colors.primary else RitimoTheme.colors.outlineVariant
    val backgroundColor = if (isSelected) RitimoTheme.colors.surfaceElevated else RitimoTheme.colors.surface

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RitimoTheme.shapes.large)
            .background(backgroundColor)
            .border(BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor), RitimoTheme.shapes.large)
            .clickable(onClick = onClick)
            .padding(RitimoTheme.spacing.lg)
    ) {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RitimoTheme.shapes.medium)
                    .background(if (isSelected) RitimoTheme.colors.primaryContainer else RitimoTheme.colors.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = if (isSelected) RitimoTheme.colors.primary else RitimoTheme.colors.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(RitimoTheme.spacing.md))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = RitimoTheme.colors.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(RitimoTheme.shapes.pill)
                                .background(RitimoTheme.colors.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = RitimoIcons.Check,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = RitimoTheme.colors.onPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

                Text(
                    text = description,
                    style = RitimoTheme.typography.bodyMedium,
                    color = RitimoTheme.colors.onSurfaceVariant
                )
            }
        }
    }
}
