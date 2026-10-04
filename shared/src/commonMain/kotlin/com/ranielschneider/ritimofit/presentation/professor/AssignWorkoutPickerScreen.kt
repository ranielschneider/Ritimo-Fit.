package com.ranielschneider.ritimofit.presentation.professor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentWorkoutAssignmentViewModel
import com.ranielschneider.ritimofit.presentation.viewmodel.WorkoutViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AssignWorkoutPickerScreen(
    studentId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    workoutViewModel: WorkoutViewModel = koinViewModel(),
    assignmentViewModel: StudentWorkoutAssignmentViewModel = koinViewModel()
) {
    LaunchedEffect(studentId) {
        workoutViewModel.loadWorkouts()
        assignmentViewModel.loadStudentAssignments(studentId)
    }

    val workoutsState by workoutViewModel.workoutsUiState.collectAsState()
    val assignedState by assignmentViewModel.assignedWorkoutsUiState.collectAsState()

    val assignedWorkoutIds = if (assignedState is UiState.Success) {
        (assignedState as UiState.Success).data.map { it.workout.id }.toSet()
    } else {
        emptySet()
    }

    RitimoAmbientBackground(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = RitimoTheme.spacing.lg)
        ) {
            Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

            // 1. Header Section
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RitimoIconButton(
                    icon = RitimoIcons.ArrowBack,
                    contentDescription = "Voltar ao perfil do aluno",
                    onClick = onBack
                )

                Spacer(modifier = Modifier.width(RitimoTheme.spacing.xs))

                RitimoStatusBadge(
                    text = "ATRIBUIR TREINO",
                    type = BadgeType.PRIMARY
                )
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

            Text(
                text = "Biblioteca de Treinos",
                style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                color = RitimoTheme.colors.onBackground
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

            Text(
                text = "Selecione um treino da sua biblioteca para atribuir a este aluno",
                style = RitimoTheme.typography.bodyMedium,
                color = RitimoTheme.colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

            // 2. Library Workouts List
            when (val state = workoutsState) {
                is UiState.Loading -> {
                    RitimoLoadingState(
                        message = "Carregando biblioteca de treinos...",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is UiState.Error -> {
                    RitimoErrorState(
                        title = "Erro ao carregar treinos",
                        message = "Não foi possível carregar os treinos da biblioteca.",
                        onRetry = { workoutViewModel.loadWorkouts() },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is UiState.Success -> {
                    val workouts = state.data
                    if (workouts.isEmpty()) {
                        RitimoEmptyState(
                            title = "Sua biblioteca está vazia",
                            description = "Crie treinos na área 'Planejar → Treinos' antes de atribuir a um aluno.",
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
                                val isAssigned = assignedWorkoutIds.contains(workout.id)

                                AssignableWorkoutCard(
                                    workout = workout,
                                    isAlreadyAssigned = isAssigned,
                                    onAssign = {
                                        assignmentViewModel.assignWorkout(
                                            studentId = studentId,
                                            workoutId = workout.id,
                                            onSuccess = onBack
                                        )
                                    },
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
private fun AssignableWorkoutCard(
    workout: Workout,
    isAlreadyAssigned: Boolean,
    onAssign: () -> Unit,
    modifier: Modifier = Modifier
) {
    RitimoGlassCard(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RitimoTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
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

            Spacer(modifier = Modifier.width(RitimoTheme.spacing.md))

            if (isAlreadyAssigned) {
                RitimoStatusBadge(
                    text = "JÁ ATRIBUÍDO",
                    type = BadgeType.NEUTRAL
                )
            } else {
                RitimoPrimaryButton(
                    text = "Atribuir",
                    onClick = onAssign
                )
            }
        }
    }
}
