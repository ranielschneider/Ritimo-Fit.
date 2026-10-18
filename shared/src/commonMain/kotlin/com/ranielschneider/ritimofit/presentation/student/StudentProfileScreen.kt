package com.ranielschneider.ritimofit.presentation.student

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ranielschneider.ritimofit.core.design.background.RitimoAmbientBackground
import com.ranielschneider.ritimofit.core.design.components.button.RitimoPrimaryButton
import com.ranielschneider.ritimofit.core.design.components.card.RitimoGlassCard
import com.ranielschneider.ritimofit.core.design.components.indicator.BadgeType
import com.ranielschneider.ritimofit.core.design.components.indicator.RitimoStatusBadge
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme

enum class StudentProfileTab {
    ExamsAndDocuments,
    MainProfile
}

@Composable
fun StudentProfileScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(StudentProfileTab.ExamsAndDocuments) }

    val tabs = listOf(
        StudentProfileTab.ExamsAndDocuments to "Exames e Documentos",
        StudentProfileTab.MainProfile to "Dados do Perfil"
    )

    Column(modifier = modifier.fillMaxSize()) {
        if (selectedTab == StudentProfileTab.ExamsAndDocuments) {
            // Secondary tab header row above Documents
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = RitimoTheme.spacing.lg)
            ) {
                Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))
                ProfileSecondaryTabBar(
                    selectedTab = selectedTab,
                    tabs = tabs,
                    onSelectTab = { selectedTab = it }
                )
            }
            StudentDocumentsScreen(modifier = Modifier.fillMaxSize())
        } else {
            RitimoAmbientBackground(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = RitimoTheme.spacing.lg)
                ) {
                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                    ProfileSecondaryTabBar(
                        selectedTab = selectedTab,
                        tabs = tabs,
                        onSelectTab = { selectedTab = it }
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                    RitimoStatusBadge(
                        text = "ÁREA DO ALUNO",
                        type = BadgeType.PRIMARY
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

                    Text(
                        text = "Seu Perfil de Aluno",
                        style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                        color = RitimoTheme.colors.onBackground
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

                    Text(
                        text = "Gerencie sua conta e sessão de usuário",
                        style = RitimoTheme.typography.bodyMedium,
                        color = RitimoTheme.colors.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

                    RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(RitimoTheme.spacing.xs)
                        ) {
                            Text(
                                text = "Encerrar Sessão",
                                style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = RitimoTheme.colors.onSurface
                            )

                            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

                            Text(
                                text = "Ao sair, você precisará autenticar novamente para acessar seu plano.",
                                style = RitimoTheme.typography.bodyMedium,
                                color = RitimoTheme.colors.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                            RitimoPrimaryButton(
                                text = "Sair da conta",
                                onClick = onLogout,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileSecondaryTabBar(
    selectedTab: StudentProfileTab,
    tabs: List<Pair<StudentProfileTab, String>>,
    onSelectTab: (StudentProfileTab) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(tabs) { (tab, label) ->
            val isSelected = tab == selectedTab
            val backgroundColor = if (isSelected) RitimoTheme.colors.primaryContainer else RitimoTheme.colors.surface
            val contentColor = if (isSelected) RitimoTheme.colors.primary else RitimoTheme.colors.onSurfaceVariant
            val borderColor = if (isSelected) RitimoTheme.colors.primary else RitimoTheme.colors.outlineVariant

            Box(
                modifier = Modifier
                    .clip(RitimoTheme.shapes.pill)
                    .background(backgroundColor)
                    .border(1.dp, borderColor, RitimoTheme.shapes.pill)
                    .clickable { onSelectTab(tab) }
                    .padding(horizontal = RitimoTheme.spacing.lg, vertical = RitimoTheme.spacing.sm)
            ) {
                Text(
                    text = label,
                    style = RitimoTheme.typography.titleMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    ),
                    color = contentColor,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}
