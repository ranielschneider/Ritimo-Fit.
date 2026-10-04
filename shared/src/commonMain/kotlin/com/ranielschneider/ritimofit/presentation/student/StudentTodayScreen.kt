package com.ranielschneider.ritimofit.presentation.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentTodayViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StudentTodayScreen(
    onViewWorkout: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StudentTodayViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    RitimoAmbientBackground(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is UiState.Loading -> {
                RitimoLoadingState(
                    message = "Carregando sua rotina...",
                    modifier = Modifier.fillMaxSize()
                )
            }
            is UiState.Error -> {
                RitimoErrorState(
                    title = "Erro ao carregar",
                    message = "Não foi possível carregar sua rotina de hoje.",
                    onRetry = { viewModel.loadTodayContent() },
                    modifier = Modifier.fillMaxSize()
                )
            }
            is UiState.Success -> {
                val data = state.data
                val studentName = data.studentUser.fullName.split(" ").firstOrNull() ?: data.studentUser.fullName
                val assignedWorkouts = data.assignedWorkouts

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = RitimoTheme.spacing.lg)
                ) {
                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

                    // 1. Header & Greeting
                    RitimoStatusBadge(
                        text = "ÁREA DO ALUNO",
                        type = BadgeType.PRIMARY
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

                    Text(
                        text = "Olá, $studentName",
                        style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                        color = RitimoTheme.colors.onBackground
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

                    Text(
                        text = "Acompanhe seu treino e metas prescritas",
                        style = RitimoTheme.typography.bodyMedium,
                        color = RitimoTheme.colors.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

                    // 2. Treino de Hoje Section
                    Text(
                        text = "Treino de hoje",
                        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = RitimoTheme.colors.onBackground
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

                    if (assignedWorkouts.isNotEmpty()) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            assignedWorkouts.forEach { workout ->
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
                                            onClick = { onViewWorkout(workout.id) },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        RitimoEmptyState(
                            title = "Nenhum treino para hoje",
                            description = "Seu professor ainda não atribuiu um treino.",
                            icon = RitimoIcons.Workouts,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxl))
                }
            }
            else -> {}
        }
    }
}
