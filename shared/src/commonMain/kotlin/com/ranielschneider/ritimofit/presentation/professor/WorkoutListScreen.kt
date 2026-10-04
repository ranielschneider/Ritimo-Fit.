package com.ranielschneider.ritimofit.presentation.professor

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
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
import com.ranielschneider.ritimofit.domain.model.Workout
import com.ranielschneider.ritimofit.presentation.base.UiState
import com.ranielschneider.ritimofit.presentation.viewmodel.WorkoutViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WorkoutListScreen(
    onCreateWorkout: () -> Unit,
    onEditWorkout: (String) -> Unit,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutViewModel = koinViewModel()
) {
    val workoutsState by viewModel.workoutsUiState.collectAsState()

    val subTabs = listOf("Treinos", "Dietas", "Exercícios")

    RitimoAmbientBackground(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = RitimoTheme.spacing.lg)
        ) {
            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

            // 1. Header Section
            RitimoStatusBadge(
                text = "RITIMO • PLANEJAMENTO",
                type = BadgeType.PRIMARY
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

            Text(
                text = "Treinos",
                style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                color = RitimoTheme.colors.onBackground
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

            Text(
                text = "Crie e organize seus treinos",
                style = RitimoTheme.typography.bodyMedium,
                color = RitimoTheme.colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

            // 2. Sub-Tabs Bar
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(subTabs) { tab ->
                    val isSelected = tab == "Treinos"
                    val backgroundColor = if (isSelected) RitimoTheme.colors.primaryContainer else RitimoTheme.colors.surface
                    val contentColor = if (isSelected) RitimoTheme.colors.primary else RitimoTheme.colors.onSurfaceVariant
                    val borderColor = if (isSelected) RitimoTheme.colors.primary else RitimoTheme.colors.outlineVariant

                    Box(
                        modifier = Modifier
                            .clip(RitimoTheme.shapes.pill)
                            .background(backgroundColor)
                            .border(1.dp, borderColor, RitimoTheme.shapes.pill)
                            .clickable { onTabSelected(tab) }
                            .padding(horizontal = RitimoTheme.spacing.lg, vertical = RitimoTheme.spacing.sm)
                    ) {
                        Text(
                            text = tab,
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

            // 3. Action Button
            RitimoPrimaryButton(
                text = "Criar treino",
                onClick = onCreateWorkout,
                icon = RitimoIcons.Add,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

            // 4. Content Section
            when (val state = workoutsState) {
                is UiState.Loading -> {
                    RitimoLoadingState(
                        message = "Carregando seus treinos...",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is UiState.Error -> {
                    RitimoErrorState(
                        title = "Erro ao carregar treinos",
                        message = "Não foi possível carregar sua lista de treinos.",
                        onRetry = { viewModel.loadWorkouts() },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is UiState.Success -> {
                    val workouts = state.data
                    if (workouts.isEmpty()) {
                        RitimoEmptyState(
                            title = "Nenhum treino criado",
                            description = "Crie seu primeiro treino para começar a montar seus planejamentos.",
                            icon = RitimoIcons.Workouts,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = workouts,
                                key = { workout -> workout.id }
                            ) { workout ->
                                WorkoutCard(
                                    workout = workout,
                                    onClick = { onEditWorkout(workout.id) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = RitimoTheme.spacing.xs)
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxl))
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun WorkoutCard(
    workout: Workout,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    RitimoGlassCard(
        modifier = modifier,
        onClick = onClick,
        padding = RitimoTheme.spacing.md
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RitimoTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RitimoTheme.shapes.medium)
                    .background(RitimoTheme.colors.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = RitimoIcons.Workouts,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = RitimoTheme.colors.primary
                )
            }

            Spacer(modifier = Modifier.width(RitimoTheme.spacing.md))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = workout.title,
                    style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = RitimoTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs)
                ) {
                    RitimoStatusBadge(
                        text = "${workout.exercises.size} EXERCÍCIOS",
                        type = BadgeType.PRIMARY
                    )

                    if (!workout.description.isNullOrBlank()) {
                        Text(
                            text = workout.description,
                            style = RitimoTheme.typography.bodySmall,
                            color = RitimoTheme.colors.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(RitimoTheme.spacing.xs))

            Icon(
                imageVector = RitimoIcons.ChevronRight,
                contentDescription = "Editar treino ${workout.title}",
                modifier = Modifier.size(20.dp),
                tint = RitimoTheme.colors.onSurfaceVariant
            )
        }
    }
}
