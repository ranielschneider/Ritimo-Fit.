package com.ranielschneider.ritimofit.presentation.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ranielschneider.ritimofit.core.design.background.RitimoAmbientBackground
import com.ranielschneider.ritimofit.core.design.components.button.RitimoIconButton
import com.ranielschneider.ritimofit.core.design.components.button.RitimoPrimaryButton
import com.ranielschneider.ritimofit.core.design.components.button.RitimoSecondaryButton
import com.ranielschneider.ritimofit.core.design.components.button.RitimoTertiaryButton
import com.ranielschneider.ritimofit.core.design.components.card.RitimoGlassCard
import com.ranielschneider.ritimofit.core.design.components.field.RitimoTextField
import com.ranielschneider.ritimofit.core.design.components.indicator.BadgeType
import com.ranielschneider.ritimofit.core.design.components.indicator.RitimoStatusBadge
import com.ranielschneider.ritimofit.core.design.components.state.RitimoEmptyState
import com.ranielschneider.ritimofit.core.design.components.state.RitimoErrorState
import com.ranielschneider.ritimofit.core.design.components.state.RitimoLoadingState
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme
import com.ranielschneider.ritimofit.domain.model.BodyMeasurementRecord
import com.ranielschneider.ritimofit.domain.model.CompletedWorkoutDetails
import com.ranielschneider.ritimofit.domain.model.EvolutionSummary
import com.ranielschneider.ritimofit.domain.model.WeightRecord
import com.ranielschneider.ritimofit.presentation.base.UiState
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentEvolutionViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StudentEvolutionScreen(
    modifier: Modifier = Modifier,
    viewModel: StudentEvolutionViewModel = koinViewModel()
) {
    val evolutionState by viewModel.evolutionUiState.collectAsState()
    val completedWorkoutsState by viewModel.completedWorkoutsState.collectAsState()

    var showAddWeightDialog by remember { mutableStateOf(false) }
    var showAddMeasurementDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    RitimoAmbientBackground(modifier = modifier.fillMaxSize()) {
        when (val state = evolutionState) {
            is UiState.Loading -> {
                RitimoLoadingState(
                    message = "Carregando sua evolução...",
                    modifier = Modifier.fillMaxSize()
                )
            }
            is UiState.Error -> {
                RitimoErrorState(
                    title = "Erro ao carregar evolução",
                    message = "Não foi possível carregar o histórico de evolução.",
                    onRetry = { viewModel.loadStudentEvolution() },
                    modifier = Modifier.fillMaxSize()
                )
            }
            is UiState.Success -> {
                val summary = state.data

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = RitimoTheme.spacing.lg)
                        .navigationBarsPadding()
                ) {
                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

                    // 1. Header
                    RitimoStatusBadge(
                        text = "ÁREA DO ALUNO",
                        type = BadgeType.PRIMARY
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

                    Text(
                        text = "Evolução Corporal",
                        style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                        color = RitimoTheme.colors.onBackground
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

                    Text(
                        text = "Acompanhe seu peso, medidas e histórico de treinos",
                        style = RitimoTheme.typography.bodyMedium,
                        color = RitimoTheme.colors.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                    // 2. Weight Summary Card & Action
                    WeightSummarySection(
                        summary = summary,
                        onAddWeight = { showAddWeightDialog = true },
                        onDeleteWeight = viewModel::deleteWeightRecord
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

                    // 3. Body Measurements Section
                    BodyMeasurementsSection(
                        records = summary.measurementRecords,
                        onAddMeasurement = { showAddMeasurementDialog = true }
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

                    // 4. Real Completed Workouts History
                    CompletedWorkoutsSection(workoutsState = completedWorkoutsState)

                    Spacer(modifier = Modifier.height(120.dp))
                }
            }
            else -> {}
        }

        // Add Weight Dialog
        if (showAddWeightDialog) {
            AddWeightDialog(
                onDismiss = { showAddWeightDialog = false },
                onConfirm = { weightKg, notes ->
                    viewModel.addWeightRecord(weightKg, notes, onSuccess = { showAddWeightDialog = false })
                }
            )
        }

        // Add Measurement Dialog
        if (showAddMeasurementDialog) {
            AddMeasurementDialog(
                onDismiss = { showAddMeasurementDialog = false },
                onConfirm = { waist, abdomen, chest, arm, thigh, hip, notes ->
                    viewModel.addMeasurementRecord(
                        waistCm = waist,
                        abdomenCm = abdomen,
                        chestCm = chest,
                        armCm = arm,
                        thighCm = thigh,
                        hipCm = hip,
                        notes = notes,
                        onSuccess = { showAddMeasurementDialog = false }
                    )
                }
            )
        }
    }
}

@Composable
private fun WeightSummarySection(
    summary: EvolutionSummary,
    onAddWeight: () -> Unit,
    onDeleteWeight: (String) -> Unit
) {
    Text(
        text = "EVOLUÇÃO DE PESO",
        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = RitimoTheme.colors.onBackground
    )

    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

    RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Peso Atual",
                        style = RitimoTheme.typography.bodySmall,
                        color = RitimoTheme.colors.onSurfaceVariant
                    )
                    Text(
                        text = summary.latestWeightKg?.let { "$it kg" } ?: "—",
                        style = RitimoTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                        color = RitimoTheme.colors.onSurface
                    )
                }

                Column {
                    Text(
                        text = "Peso Inicial",
                        style = RitimoTheme.typography.bodySmall,
                        color = RitimoTheme.colors.onSurfaceVariant
                    )
                    Text(
                        text = summary.initialWeightKg?.let { "$it kg" } ?: "—",
                        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = RitimoTheme.colors.onSurface
                    )
                }

                Column {
                    Text(
                        text = "Diferença",
                        style = RitimoTheme.typography.bodySmall,
                        color = RitimoTheme.colors.onSurfaceVariant
                    )
                    val diff = summary.weightDifferenceKg
                    val diffStr = when {
                        diff == null -> "—"
                        diff > 0 -> "+$diff kg"
                        else -> "$diff kg"
                    }
                    RitimoStatusBadge(
                        text = diffStr,
                        type = if (diff != null && diff <= 0) BadgeType.PRIMARY else BadgeType.NEUTRAL
                    )
                }
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

            RitimoPrimaryButton(
                text = "+ Registrar Peso",
                onClick = onAddWeight,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (summary.weightRecords.isNotEmpty()) {
        Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))
        Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs)) {
            summary.weightRecords.forEach { record ->
                RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${record.weightKg} kg",
                                style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = RitimoTheme.colors.onSurface
                            )
                            if (!record.notes.isNullOrBlank()) {
                                Text(
                                    text = record.notes,
                                    style = RitimoTheme.typography.bodySmall,
                                    color = RitimoTheme.colors.onSurfaceVariant
                                )
                            }
                        }

                        RitimoIconButton(
                            icon = RitimoIcons.Trash,
                            contentDescription = "Excluir registro de peso",
                            onClick = { onDeleteWeight(record.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BodyMeasurementsSection(
    records: List<BodyMeasurementRecord>,
    onAddMeasurement: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "MEDIDAS CORPORAIS",
            style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = RitimoTheme.colors.onBackground
        )

        RitimoSecondaryButton(
            text = "+ Registrar Medidas",
            onClick = onAddMeasurement
        )
    }

    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

    if (records.isEmpty()) {
        RitimoEmptyState(
            title = "Nenhuma medida registrada",
            description = "Registre suas medidas corporais para acompanhar seu progresso físico.",
            icon = RitimoIcons.Evolution,
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
            records.forEach { record ->
                RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        if (!record.notes.isNullOrBlank()) {
                            Text(
                                text = record.notes,
                                style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = RitimoTheme.colors.onSurface
                            )
                            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs)
                        ) {
                            record.waistCm?.let { RitimoStatusBadge(text = "Cintura: ${it}cm", type = BadgeType.PRIMARY) }
                            record.abdomenCm?.let { RitimoStatusBadge(text = "Abdómen: ${it}cm", type = BadgeType.NEUTRAL) }
                            record.chestCm?.let { RitimoStatusBadge(text = "Peito: ${it}cm", type = BadgeType.NEUTRAL) }
                        }

                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs)
                        ) {
                            record.armCm?.let { RitimoStatusBadge(text = "Braço: ${it}cm", type = BadgeType.NEUTRAL) }
                            record.thighCm?.let { RitimoStatusBadge(text = "Coxa: ${it}cm", type = BadgeType.NEUTRAL) }
                            record.hipCm?.let { RitimoStatusBadge(text = "Anca: ${it}cm", type = BadgeType.NEUTRAL) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CompletedWorkoutsSection(
    workoutsState: UiState<List<CompletedWorkoutDetails>>
) {
    Text(
        text = "HISTÓRICO DE TREINOS",
        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = RitimoTheme.colors.onBackground
    )

    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

    when (workoutsState) {
        is UiState.Success -> {
            val list = workoutsState.data
            if (list.isEmpty()) {
                RitimoEmptyState(
                    title = "Nenhum treino concluído ainda",
                    description = "Seus treinos executados aparecerão automaticamente nesta seção.",
                    icon = RitimoIcons.Workouts,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
                    list.forEach { details ->
                        val dur = details.durationSeconds
                        val min = dur / 60
                        val sec = dur % 60
                        val durStr = if (min > 0) "${min}m ${sec}s" else "${sec}s"

                        RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = details.workout.title,
                                        style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = RitimoTheme.colors.onSurface
                                    )
                                    Text(
                                        text = "Duração: $durStr",
                                        style = RitimoTheme.typography.bodyMedium,
                                        color = RitimoTheme.colors.primary
                                    )
                                }

                                RitimoStatusBadge(text = "CONCLUÍDO", type = BadgeType.PRIMARY)
                            }
                        }
                    }
                }
            }
        }
        else -> {}
    }
}

@Composable
private fun AddWeightDialog(
    onDismiss: () -> Unit,
    onConfirm: (weightKg: Double, notes: String?) -> Unit
) {
    var weightStr by remember { mutableStateOf("") }
    var notesStr by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Registrar Peso",
                style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
                RitimoTextField(
                    value = weightStr,
                    onValueChange = {
                        weightStr = it
                        errorMsg = null
                    },
                    label = "Peso (kg)",
                    placeholder = "Ex.: 79.5",
                    errorMessage = errorMsg,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                RitimoTextField(
                    value = notesStr,
                    onValueChange = { notesStr = it },
                    label = "Observação (Opcional)",
                    placeholder = "Ex.: Jejum / Pós-treino"
                )
            }
        },
        confirmButton = {
            RitimoPrimaryButton(
                text = "Salvar Peso",
                onClick = {
                    val weight = weightStr.toDoubleOrNull()
                    if (weight != null && weight > 0.0) {
                        onConfirm(weight, notesStr)
                    } else {
                        errorMsg = "Informe um peso válido maior que zero."
                    }
                }
            )
        },
        dismissButton = {
            RitimoTertiaryButton(
                text = "Cancelar",
                onClick = onDismiss
            )
        }
    )
}

@Composable
private fun AddMeasurementDialog(
    onDismiss: () -> Unit,
    onConfirm: (waist: Double?, abdomen: Double?, chest: Double?, arm: Double?, thigh: Double?, hip: Double?, notes: String?) -> Unit
) {
    var waist by remember { mutableStateOf("") }
    var abdomen by remember { mutableStateOf("") }
    var chest by remember { mutableStateOf("") }
    var arm by remember { mutableStateOf("") }
    var thigh by remember { mutableStateOf("") }
    var hip by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Registrar Medidas (cm)",
                style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.sm)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.sm)) {
                    RitimoTextField(
                        value = waist,
                        onValueChange = { waist = it },
                        label = "Cintura (cm)",
                        placeholder = "84.0",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    RitimoTextField(
                        value = abdomen,
                        onValueChange = { abdomen = it },
                        label = "Abdómen (cm)",
                        placeholder = "87.5",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.sm)) {
                    RitimoTextField(
                        value = chest,
                        onValueChange = { chest = it },
                        label = "Peito (cm)",
                        placeholder = "104.0",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    RitimoTextField(
                        value = arm,
                        onValueChange = { arm = it },
                        label = "Braço (cm)",
                        placeholder = "39.5",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.sm)) {
                    RitimoTextField(
                        value = thigh,
                        onValueChange = { thigh = it },
                        label = "Coxa (cm)",
                        placeholder = "59.5",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    RitimoTextField(
                        value = hip,
                        onValueChange = { hip = it },
                        label = "Anca/Quadril (cm)",
                        placeholder = "96.0",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                RitimoTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = "Observação (Opcional)",
                    placeholder = "Ex.: Avaliação do mês"
                )
            }
        },
        confirmButton = {
            RitimoPrimaryButton(
                text = "Salvar Medidas",
                onClick = {
                    onConfirm(
                        waist.toDoubleOrNull(),
                        abdomen.toDoubleOrNull(),
                        chest.toDoubleOrNull(),
                        arm.toDoubleOrNull(),
                        thigh.toDoubleOrNull(),
                        hip.toDoubleOrNull(),
                        notes
                    )
                }
            )
        },
        dismissButton = {
            RitimoTertiaryButton(
                text = "Cancelar",
                onClick = onDismiss
            )
        }
    )
}
