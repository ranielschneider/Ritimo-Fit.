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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.ranielschneider.ritimofit.core.design.background.RitimoAmbientBackground
import com.ranielschneider.ritimofit.core.design.components.button.RitimoIconButton
import com.ranielschneider.ritimofit.core.design.components.button.RitimoPrimaryButton
import com.ranielschneider.ritimofit.core.design.components.button.RitimoSecondaryButton
import com.ranielschneider.ritimofit.core.design.components.card.RitimoGlassCard
import com.ranielschneider.ritimofit.core.design.components.field.RitimoTextField
import com.ranielschneider.ritimofit.core.design.components.indicator.BadgeType
import com.ranielschneider.ritimofit.core.design.components.indicator.RitimoStatusBadge
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme
import com.ranielschneider.ritimofit.domain.model.WorkoutExercise
import com.ranielschneider.ritimofit.presentation.viewmodel.WorkoutViewModel

@Composable
fun WorkoutEditorScreen(
    workoutIdToEdit: String?,
    onBack: () -> Unit,
    onAddExerciseFromCatalog: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutViewModel
) {
    LaunchedEffect(workoutIdToEdit) {
        if (workoutIdToEdit != null && viewModel.currentWorkoutId.value != workoutIdToEdit) {
            viewModel.loadWorkoutForEditing(workoutIdToEdit)
        }
    }

    val workoutTitle by viewModel.workoutTitle.collectAsState()
    val workoutDescription by viewModel.workoutDescription.collectAsState()
    val exercises by viewModel.workoutExercises.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val saveError by viewModel.saveError.collectAsState()
    val currentId by viewModel.currentWorkoutId.collectAsState()

    val scrollState = rememberScrollState()
    val isEditMode = currentId != null

    RitimoAmbientBackground(modifier = modifier.fillMaxSize()) {
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
                    contentDescription = "Voltar para lista de treinos",
                    onClick = onBack
                )

                Spacer(modifier = Modifier.width(RitimoTheme.spacing.xs))

                RitimoStatusBadge(
                    text = if (isEditMode) "EDITAR TREINO" else "NOVO TREINO",
                    type = BadgeType.PRIMARY
                )
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

            Text(
                text = if (isEditMode) "Editar Treino" else "Criar Treino",
                style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                color = RitimoTheme.colors.onBackground
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

            Text(
                text = "Defina o nome, os exercícios e as configurações de séries/repetições",
                style = RitimoTheme.typography.bodyMedium,
                color = RitimoTheme.colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

            // 2. Form Fields Section
            RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
                    RitimoTextField(
                        value = workoutTitle,
                        onValueChange = viewModel::onTitleChanged,
                        label = "Nome do treino",
                        placeholder = "Ex.: Treino A — Peito e Tríceps",
                        errorMessage = saveError,
                        modifier = Modifier.fillMaxWidth()
                    )

                    RitimoTextField(
                        value = workoutDescription,
                        onValueChange = viewModel::onDescriptionChanged,
                        label = "Descrição (Opcional)",
                        placeholder = "Ex.: Foco em peitoral superior e carga moderada",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

            // 3. Exercises Header & Catalog Add Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Exercícios do Treino",
                    style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = RitimoTheme.colors.onBackground
                )

                RitimoSecondaryButton(
                    text = "+ Adicionar",
                    onClick = onAddExerciseFromCatalog
                )
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

            // 4. Configured Exercises List
            if (exercises.isEmpty()) {
                RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(RitimoTheme.spacing.md),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Nenhum exercício adicionado",
                            style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = RitimoTheme.colors.onSurface
                        )
                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
                        Text(
                            text = "Toque em '+ Adicionar' para escolher exercícios do catálogo e montar este treino.",
                            style = RitimoTheme.typography.bodyMedium,
                            color = RitimoTheme.colors.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
                    exercises.forEachIndexed { index, exerciseConfig ->
                        WorkoutExerciseConfigCard(
                            index = index,
                            totalCount = exercises.size,
                            exerciseConfig = exerciseConfig,
                            onUpdateConfig = { sets, reps, weightKg, restSeconds, notes ->
                                viewModel.updateExerciseConfig(
                                    index = index,
                                    sets = sets,
                                    reps = reps,
                                    weightKg = weightKg,
                                    restSeconds = restSeconds,
                                    notes = notes
                                )
                            },
                            onMoveUp = { viewModel.moveExerciseUp(index) },
                            onMoveDown = { viewModel.moveExerciseDown(index) },
                            onRemove = { viewModel.removeExercise(index) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

            // 5. Save Button
            RitimoPrimaryButton(
                text = if (isSaving) "Salvando..." else "Salvar treino",
                onClick = { viewModel.saveWorkout(onSuccess = onBack) },
                enabled = !isSaving,
                icon = RitimoIcons.Check,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxl))
        }
    }
}

@Composable
private fun WorkoutExerciseConfigCard(
    index: Int,
    totalCount: Int,
    exerciseConfig: WorkoutExercise,
    onUpdateConfig: (sets: Int, reps: String, weightKg: Double?, restSeconds: Int, notes: String?) -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    RitimoGlassCard(modifier = modifier) {
        Column {
            // Card Top Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
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

                // Move Up / Move Down / Remove Actions
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (index > 0) {
                        RitimoIconButton(
                            icon = RitimoIcons.ArrowUp,
                            contentDescription = "Mover para cima",
                            onClick = onMoveUp
                        )
                    }
                    if (index < totalCount - 1) {
                        RitimoIconButton(
                            icon = RitimoIcons.ArrowDown,
                            contentDescription = "Mover para baixo",
                            onClick = onMoveDown
                        )
                    }
                    RitimoIconButton(
                        icon = RitimoIcons.Trash,
                        contentDescription = "Remover do treino",
                        onClick = onRemove
                    )
                }
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

            // Config Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.sm)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    RitimoTextField(
                        value = exerciseConfig.sets.toString(),
                        onValueChange = { newValue ->
                            val sets = newValue.toIntOrNull() ?: 1
                            onUpdateConfig(sets, exerciseConfig.reps, exerciseConfig.weightKg, exerciseConfig.restSeconds, exerciseConfig.notes)
                        },
                        label = "Séries",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    RitimoTextField(
                        value = exerciseConfig.reps,
                        onValueChange = { reps ->
                            onUpdateConfig(exerciseConfig.sets, reps, exerciseConfig.weightKg, exerciseConfig.restSeconds, exerciseConfig.notes)
                        },
                        label = "Repetições"
                    )
                }
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.sm)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    RitimoTextField(
                        value = exerciseConfig.weightKg?.toString().orEmpty(),
                        onValueChange = { weightStr ->
                            val weight = weightStr.toDoubleOrNull()
                            onUpdateConfig(exerciseConfig.sets, exerciseConfig.reps, weight, exerciseConfig.restSeconds, exerciseConfig.notes)
                        },
                        label = "Carga (kg)",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    RitimoTextField(
                        value = exerciseConfig.restSeconds.toString(),
                        onValueChange = { restStr ->
                            val rest = restStr.toIntOrNull() ?: 60
                            onUpdateConfig(exerciseConfig.sets, exerciseConfig.reps, exerciseConfig.weightKg, rest, exerciseConfig.notes)
                        },
                        label = "Descanso (s)",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

            RitimoTextField(
                value = exerciseConfig.notes.orEmpty(),
                onValueChange = { notes ->
                    onUpdateConfig(exerciseConfig.sets, exerciseConfig.reps, exerciseConfig.weightKg, exerciseConfig.restSeconds, notes.takeIf { it.isNotBlank() })
                },
                label = "Observações do exercício (Opcional)",
                placeholder = "Ex.: Cadência 3-0-1-0 ou descer até 90°",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
