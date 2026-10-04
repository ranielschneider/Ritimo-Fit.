package com.ranielschneider.ritimofit.presentation.student

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.ranielschneider.ritimofit.core.design.components.state.RitimoEmptyState
import com.ranielschneider.ritimofit.core.design.components.state.RitimoErrorState
import com.ranielschneider.ritimofit.core.design.components.state.RitimoLoadingState
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme
import com.ranielschneider.ritimofit.presentation.base.UiState
import com.ranielschneider.ritimofit.presentation.navigation.WorkoutsSubRoute
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentWorkoutsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StudentWorkoutsScreen(
    onViewWorkoutDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StudentWorkoutsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedSubTab by viewModel.selectedSubTab.collectAsState()
    val scrollState = rememberScrollState()

    val subTabs = listOf(
        WorkoutsSubRoute.TodayWorkout() to "Hoje",
        WorkoutsSubRoute.UpcomingWorkouts to "Próximos",
        WorkoutsSubRoute.History to "Histórico"
    )

    RitimoAmbientBackground(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = RitimoTheme.spacing.lg)
        ) {
            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

            // 1. Header
            RitimoStatusBadge(
                text = "ÁREA DO ALUNO",
                type = BadgeType.PRIMARY
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

            Text(
                text = "Seus Treinos",
                style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                color = RitimoTheme.colors.onBackground
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

            Text(
                text = "Acesse os treinos prescritos e acompanhe seu planejamento",
                style = RitimoTheme.typography.bodyMedium,
                color = RitimoTheme.colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

            // 2. Sub-Tabs Bar
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(subTabs) { (subRoute, label) ->
                    val isSelected = (subRoute is WorkoutsSubRoute.TodayWorkout && selectedSubTab is WorkoutsSubRoute.TodayWorkout) ||
                            (subRoute == selectedSubTab)
                    val backgroundColor = if (isSelected) RitimoTheme.colors.primaryContainer else RitimoTheme.colors.surface
                    val contentColor = if (isSelected) RitimoTheme.colors.primary else RitimoTheme.colors.onSurfaceVariant
                    val borderColor = if (isSelected) RitimoTheme.colors.primary else RitimoTheme.colors.outlineVariant

                    Box(
                        modifier = Modifier
                            .clip(RitimoTheme.shapes.pill)
                            .background(backgroundColor)
                            .border(1.dp, borderColor, RitimoTheme.shapes.pill)
                            .clickable { viewModel.onSubTabSelected(subRoute) }
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

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

            // 3. Tab Content
            when (selectedSubTab) {
                is WorkoutsSubRoute.TodayWorkout -> {
                    when (val state = uiState) {
                        is UiState.Loading -> {
                            RitimoLoadingState(
                                message = "Carregando seus treinos...",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        is UiState.Error -> {
                            RitimoErrorState(
                                title = "Erro ao carregar treinos",
                                message = "Não foi possível carregar seus treinos prescritos.",
                                onRetry = { viewModel.loadStudentWorkouts() },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        is UiState.Success -> {
                            val assignedDetails = state.data
                            if (assignedDetails.isEmpty()) {
                                RitimoEmptyState(
                                    title = "Nenhum treino atribuído",
                                    description = "Seu professor ainda não atribuiu um treino.",
                                    icon = RitimoIcons.Workouts,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    assignedDetails.forEach { details ->
                                        val workout = details.workout
                                        RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(RitimoTheme.spacing.xs)
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = workout.title,
                                                        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                                        color = RitimoTheme.colors.onSurface
                                                    )

                                                    RitimoStatusBadge(
                                                        text = "${workout.exercises.size} EXERCÍCIOS",
                                                        type = BadgeType.PRIMARY
                                                    )
                                                }

                                                if (!workout.description.isNullOrBlank()) {
                                                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
                                                    Text(
                                                        text = workout.description,
                                                        style = RitimoTheme.typography.bodyMedium,
                                                        color = RitimoTheme.colors.onSurfaceVariant
                                                    )
                                                }

                                                Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                                                RitimoPrimaryButton(
                                                    text = "Ver treino",
                                                    onClick = { onViewWorkoutDetail(workout.id) },
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else -> {}
                    }
                }
                is WorkoutsSubRoute.UpcomingWorkouts -> {
                    RitimoEmptyState(
                        title = "Nenhum treino agendado",
                        description = "Os treinos futuros aparecerão aqui quando forem agendados.",
                        icon = RitimoIcons.Workouts,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                is WorkoutsSubRoute.History -> {
                    RitimoEmptyState(
                        title = "Nenhum histórico ainda",
                        description = "Seu histórico de treinos concluídos aparecerá aqui.",
                        icon = RitimoIcons.Workouts,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxl))
        }
    }
}
