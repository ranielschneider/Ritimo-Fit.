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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ranielschneider.ritimofit.core.design.background.RitimoAmbientBackground
import com.ranielschneider.ritimofit.core.design.components.button.RitimoIconButton
import com.ranielschneider.ritimofit.core.design.components.button.RitimoPrimaryButton
import com.ranielschneider.ritimofit.core.design.components.button.RitimoTertiaryButton
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
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

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
    val activeSessionId by viewModel.activeWorkoutSessionId.collectAsState()
    val sessionStartMs by viewModel.sessionStartTimestamp.collectAsState()
    val sessionConflict by viewModel.sessionConflict.collectAsState()

    val isSessionActive = activeSessionId == workoutId

    var nowMs by remember { mutableStateOf(Clock.System.now().toEpochMilliseconds()) }

    LaunchedEffect(isSessionActive) {
        while (isSessionActive) {
            nowMs = Clock.System.now().toEpochMilliseconds()
            delay(500)
        }
    }

    val elapsedSec = if (isSessionActive && sessionStartMs > 0) {
        ((nowMs - sessionStartMs) / 1000).coerceAtLeast(0)
    } else 0L

    val min = elapsedSec / 60
    val sec = elapsedSec % 60
    val formattedDuration = "${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}"

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
                            text = if (isSessionActive) "SESSÃO EM EXECUÇÃO" else "TREINO PRESCRITO",
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

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                    // 2. Execution Timer & Action Bar
                    RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (isSessionActive) {
                                RitimoStatusBadge(
                                    text = "EM EXECUÇÃO $formattedDuration",
                                    type = BadgeType.PRIMARY
                                )

                                Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

                                RitimoPrimaryButton(
                                    text = "Concluir treino",
                                    onClick = {
                                        viewModel.completeActiveWorkoutSession(
                                            workoutId = workoutId,
                                            onSuccess = onBack
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else {
                                RitimoPrimaryButton(
                                    text = "Começar treino",
                                    onClick = { viewModel.startWorkoutSession(workoutId) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

                    // 3. Exercise List Header
                    Text(
                        text = "Exercícios do Treino (${workout.exercises.size})",
                        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = RitimoTheme.colors.onBackground
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

                    // 4. Read-Only Exercise Items
                    Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
                        workout.exercises.forEachIndexed { index, exerciseConfig ->
                            ReadOnlyExerciseCard(
                                index = index,
                                exerciseConfig = exerciseConfig,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(120.dp))
                }
            }
            else -> {}
        }

        // Active Session Conflict Dialog
        sessionConflict?.let { conflict ->
            AlertDialog(
                onDismissRequest = { viewModel.dismissSessionConflict() },
                title = {
                    Text(
                        text = "Treino em andamento",
                        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                text = {
                    Text(
                        text = "Você já possui uma sessão de treino ativa. Deseja continuar o treino atual ou finalizá-lo para iniciar este novo treino?",
                        style = RitimoTheme.typography.bodyMedium,
                        color = RitimoTheme.colors.onSurfaceVariant
                    )
                },
                confirmButton = {
                    RitimoPrimaryButton(
                        text = "Finalizar e iniciar novo",
                        onClick = {
                            viewModel.resolveConflictFinishAndStartNew()
                        }
                    )
                },
                dismissButton = {
                    RitimoTertiaryButton(
                        text = "Continuar treino atual",
                        onClick = { viewModel.dismissSessionConflict() }
                    )
                }
            )
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
