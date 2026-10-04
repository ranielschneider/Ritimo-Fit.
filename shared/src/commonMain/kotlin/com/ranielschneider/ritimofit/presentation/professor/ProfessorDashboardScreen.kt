package com.ranielschneider.ritimofit.presentation.professor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.ranielschneider.ritimofit.core.design.background.RitimoAmbientBackground
import com.ranielschneider.ritimofit.core.design.components.card.RitimoGlassCard
import com.ranielschneider.ritimofit.core.design.components.card.RitimoStandardCard
import com.ranielschneider.ritimofit.core.design.components.indicator.BadgeType
import com.ranielschneider.ritimofit.core.design.components.indicator.RitimoStatusBadge
import com.ranielschneider.ritimofit.core.design.components.state.RitimoErrorState
import com.ranielschneider.ritimofit.core.design.components.state.RitimoLoadingState
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme
import com.ranielschneider.ritimofit.presentation.base.UiState
import com.ranielschneider.ritimofit.presentation.navigation.PlanningSubRoute
import com.ranielschneider.ritimofit.presentation.navigation.ProfessorRoute
import com.ranielschneider.ritimofit.presentation.navigation.StudentsSubRoute
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfessorDashboardScreen(
    onNavigateToRoute: (ProfessorRoute) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfessorDashboardViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    RitimoAmbientBackground(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is UiState.Loading -> {
                RitimoLoadingState(message = "Carregando seu painel...")
            }
            is UiState.Error -> {
                RitimoErrorState(
                    title = "Falha ao carregar Dashboard",
                    message = "Não foi possível carregar os dados. Tente novamente.",
                    onRetry = { viewModel.loadDashboardData() }
                )
            }
            is UiState.Success -> {
                val data = state.data
                val greeting = if (data.professorName != null) {
                    "Bom dia, ${data.professorName}"
                } else {
                    "Bom dia"
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = RitimoTheme.spacing.lg, vertical = RitimoTheme.spacing.xl)
                ) {
                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                    // 1. Header Section
                    DashboardHeader(
                        greeting = greeting,
                        onProfileClick = { onNavigateToRoute(ProfessorRoute.Profile()) }
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

                    // 2. Resumo Section
                    Text(
                        text = "Resumo Geral",
                        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = RitimoTheme.colors.onBackground
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.sm))

                    DashboardSummaryGrid(summary = data.summary)

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

                    // 3. Atenção Section
                    Text(
                        text = "Atenção Necessária",
                        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = RitimoTheme.colors.onBackground
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.sm))

                    RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(RitimoTheme.spacing.xs)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RitimoTheme.shapes.medium)
                                    .background(RitimoTheme.colors.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = RitimoIcons.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = RitimoTheme.colors.primary
                                )
                            }

                            Spacer(modifier = Modifier.width(RitimoTheme.spacing.md))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Tudo em dia",
                                    style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = RitimoTheme.colors.onSurface
                                )
                                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))
                                Text(
                                    text = "Nenhum aluno requer atenção imediata no momento.",
                                    style = RitimoTheme.typography.bodyMedium,
                                    color = RitimoTheme.colors.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

                    // 4. Atividade Recente Section
                    Text(
                        text = "Atividade Recente",
                        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = RitimoTheme.colors.onBackground
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.sm))

                    RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(RitimoTheme.spacing.xs)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RitimoTheme.shapes.medium)
                                    .background(RitimoTheme.colors.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = RitimoIcons.Activity,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = RitimoTheme.colors.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.width(RitimoTheme.spacing.md))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Nenhuma atividade recente",
                                    style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = RitimoTheme.colors.onSurface
                                )
                                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))
                                Text(
                                    text = "As atualizações e treinos concluídos dos seus alunos aparecerão aqui.",
                                    style = RitimoTheme.typography.bodyMedium,
                                    color = RitimoTheme.colors.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

                    // 5. Ações Rápidas Section
                    Text(
                        text = "Ações Rápidas",
                        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = RitimoTheme.colors.onBackground
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.sm))

                    Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.sm)) {
                        QuickActionCard(
                            title = "Novo Aluno",
                            subtitle = "Cadastre um novo aluno na sua consultoria",
                            icon = RitimoIcons.Add,
                            onClick = { onNavigateToRoute(ProfessorRoute.Students(StudentsSubRoute.List)) }
                        )

                        QuickActionCard(
                            title = "Criar Treino",
                            subtitle = "Monte um novo plano de treino personalizado",
                            icon = RitimoIcons.Workouts,
                            onClick = { onNavigateToRoute(ProfessorRoute.Planning(PlanningSubRoute.Workouts)) }
                        )

                        QuickActionCard(
                            title = "Criar Dieta",
                            subtitle = "Prescreva um plano alimentar para seu aluno",
                            icon = RitimoIcons.Diet,
                            onClick = { onNavigateToRoute(ProfessorRoute.Planning(PlanningSubRoute.Diets)) }
                        )
                    }

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxl))
                }
            }
            else -> {}
        }
    }
}

@Composable
private fun DashboardHeader(
    greeting: String,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            RitimoStatusBadge(
                text = "PAINEL DO PROFESSOR",
                type = BadgeType.PRIMARY
            )
            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
            Text(
                text = greeting,
                style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                color = RitimoTheme.colors.onBackground
            )
        }

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RitimoTheme.shapes.pill)
                .background(RitimoTheme.colors.surfaceElevated)
                .clickable(onClick = onProfileClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = RitimoIcons.User,
                contentDescription = "Perfil",
                modifier = Modifier.size(24.dp),
                tint = RitimoTheme.colors.primary
            )
        }
    }
}

@Composable
private fun DashboardSummaryGrid(
    summary: DashboardSummary
) {
    Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.sm)) {
        Row(horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.sm)) {
            SummaryItemCard(
                title = "Alunos ativos",
                value = summary.activeStudents,
                description = "Sem dados",
                icon = RitimoIcons.Students,
                modifier = Modifier.weight(1f)
            )

            SummaryItemCard(
                title = "Precisam atenção",
                value = summary.pendingAttention,
                description = "Sem pendências",
                icon = RitimoIcons.Warning,
                modifier = Modifier.weight(1f)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.sm)) {
            SummaryItemCard(
                title = "Treinos hoje",
                value = summary.todayWorkouts,
                description = "Agendados",
                icon = RitimoIcons.Workouts,
                modifier = Modifier.weight(1f)
            )

            SummaryItemCard(
                title = "Adesão média",
                value = summary.adherenceRate,
                description = "Geral",
                icon = RitimoIcons.Activity,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SummaryItemCard(
    title: String,
    value: String,
    description: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    RitimoStandardCard(
        modifier = modifier,
        padding = RitimoTheme.spacing.md
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = RitimoTheme.colors.primary
                )
                Spacer(modifier = Modifier.width(RitimoTheme.spacing.xs))
                Text(
                    text = title,
                    style = RitimoTheme.typography.bodySmall,
                    color = RitimoTheme.colors.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

            Text(
                text = value,
                style = RitimoTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                color = RitimoTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

            Text(
                text = description,
                style = RitimoTheme.typography.caption,
                color = RitimoTheme.colors.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    RitimoStandardCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RitimoTheme.shapes.medium)
                    .background(RitimoTheme.colors.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = RitimoTheme.colors.primary
                )
            }

            Spacer(modifier = Modifier.width(RitimoTheme.spacing.md))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = RitimoTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))
                Text(
                    text = subtitle,
                    style = RitimoTheme.typography.bodySmall,
                    color = RitimoTheme.colors.onSurfaceVariant
                )
            }
        }
    }
}
