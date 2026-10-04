package com.ranielschneider.ritimofit.presentation.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.ranielschneider.ritimofit.core.design.background.RitimoAmbientBackground
import com.ranielschneider.ritimofit.core.design.components.button.RitimoIconButton
import com.ranielschneider.ritimofit.core.design.components.card.RitimoGlassCard
import com.ranielschneider.ritimofit.core.design.components.indicator.BadgeType
import com.ranielschneider.ritimofit.core.design.components.indicator.RitimoStatusBadge
import com.ranielschneider.ritimofit.core.design.components.state.RitimoErrorState
import com.ranielschneider.ritimofit.core.design.components.state.RitimoLoadingState
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme
import com.ranielschneider.ritimofit.domain.model.WorkoutExercise
import com.ranielschneider.ritimofit.presentation.base.UiState
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentTodayViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StudentWorkoutDetailScreen(
    workoutId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StudentTodayViewModel = koinViewModel()
) {
    LaunchedEffect(workoutId) {
        viewModel.loadReadOnlyWorkout(workoutId)
    }

    val workoutState by viewModel.readOnlyWorkoutState.collectAsState()
    val scrollState = rememberScrollState()

    RitimoAmbientBackground(modifier = modifier.fillMaxSize()) {
        when (val state = workoutState) {
            is UiState.Loading -> {
                RitimoLoadingState(
                    message = "Carregando detalhes do treino...",
                    modifier = Modifier.fillMaxSize()
                )
            }
            is UiState.Error -> {
                RitimoErrorState(
                    title = "Treino não encontrado",
                    message = "Não foi possível carregar as informações deste treino.",
                    onRetry = { viewModel.loadReadOnlyWorkout(workoutId) },
                    modifier = Modifier.fillMaxSize()
                )
            }
            is UiState.Success -> {
                val workout = state.data

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = RitimoTheme.spacing.lg)
                ) {
                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                    // 1. Header Section
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RitimoIconButton(
                            icon = RitimoIcons.ArrowBack,
                            contentDescription = "Voltar para tela de Hoje",
                            onClick = onBack
                        )

                        Spacer(modifier = Modifier.width(RitimoTheme.spacing.xs))

                        RitimoStatusBadge(
                            text = "TREINO PRESCRITO",
                            type = BadgeType.PRIMARY
                        )
                    }

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

                    Text(
                        text = workout.title,
                        style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                        color = RitimoTheme.colors.onBackground
                    )

                    if (!workout.description.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))
                        Text(
                            text = workout.description,
                            style = RitimoTheme.typography.bodyMedium,
                            color = RitimoTheme.colors.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

                    // 2. Exercise List Header
                    Text(
                        text = "Exercícios do Treino (${workout.exercises.size})",
                        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = RitimoTheme.colors.onBackground
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

                    // 3. Read-Only Exercise Items
                    Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
                        workout.exercises.forEachIndexed { index, exerciseConfig ->
                            ReadOnlyExerciseCard(
                                index = index,
                                exerciseConfig = exerciseConfig,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxl))
                }
            }
            else -> {}
        }
    }
}

@Composable
private fun ReadOnlyExerciseCard(
    index: Int,
    exerciseConfig: WorkoutExercise,
    modifier: Modifier = Modifier
) {
    RitimoGlassCard(modifier = modifier) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RitimoStatusBadge(
                    text = "#${index + 1}",
                    type = BadgeType.PRIMARY
                )

                Spacer(modifier = Modifier.width(RitimoTheme.spacing.xs))

                Column {
                    Text(
                        text = exerciseConfig.exerciseName,
                        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = RitimoTheme.colors.onSurface
                    )

                    if (!exerciseConfig.muscleGroup.isNullOrBlank()) {
                        Text(
                            text = exerciseConfig.muscleGroup + (exerciseConfig.equipment?.let { " • $it" } ?: ""),
                            style = RitimoTheme.typography.bodySmall,
                            color = RitimoTheme.colors.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

            // Badges Row for Config Values
            Row(
                horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RitimoStatusBadge(
                    text = "${exerciseConfig.sets} SÉRIES",
                    type = BadgeType.PRIMARY
                )

                RitimoStatusBadge(
                    text = "${exerciseConfig.reps} REPS",
                    type = BadgeType.NEUTRAL
                )

                if (exerciseConfig.weightKg != null && exerciseConfig.weightKg > 0.0) {
                    RitimoStatusBadge(
                        text = "${exerciseConfig.weightKg} KG",
                        type = BadgeType.NEUTRAL
                    )
                }

                RitimoStatusBadge(
                    text = "${exerciseConfig.restSeconds}S DESCANSO",
                    type = BadgeType.NEUTRAL
                )
            }

            if (!exerciseConfig.notes.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
                Text(
                    text = "Obs.: ${exerciseConfig.notes}",
                    style = RitimoTheme.typography.bodyMedium,
                    color = RitimoTheme.colors.onSurfaceVariant
                )
            }
        }
    }
}
