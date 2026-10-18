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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
import com.ranielschneider.ritimofit.domain.model.Student
import com.ranielschneider.ritimofit.presentation.base.UiState
import com.ranielschneider.ritimofit.presentation.navigation.StudentDetailTab
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentDetailViewModel
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentDietAssignmentViewModel
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentDocumentViewModel
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentEvolutionViewModel
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentWorkoutAssignmentViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StudentDetailScreen(
    studentId: String,
    onBack: () -> Unit,
    onNavigateToAssignWorkoutPicker: () -> Unit,
    onNavigateToAssignDietPicker: () -> Unit,
    onViewDietDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StudentDetailViewModel = koinViewModel()
) {
    LaunchedEffect(studentId) {
        viewModel.loadStudent(studentId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val scrollState = rememberScrollState()

    RitimoAmbientBackground(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is UiState.Loading -> {
                RitimoLoadingState(
                    message = "Carregando dados do aluno...",
                    modifier = Modifier.fillMaxSize()
                )
            }
            is UiState.Error -> {
                RitimoErrorState(
                    title = "Aluno não encontrado",
                    message = "Não foi possível carregar as informações deste aluno.",
                    onRetry = { viewModel.loadStudent(studentId) },
                    modifier = Modifier.fillMaxSize()
                )
            }
            is UiState.Success -> {
                val student = state.data.student

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = RitimoTheme.spacing.lg)
                ) {
                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                    // 1. Header do Aluno
                    StudentDetailHeader(
                        student = student,
                        onBack = onBack
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                    // 2. Navegação Interna Secundária (7 Abas)
                    StudentSecondaryTabBar(
                        selectedTab = selectedTab,
                        onTabSelected = viewModel::onTabSelected
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                    // 3. Conteúdo da Aba Selecionada
                    when (selectedTab) {
                        StudentDetailTab.Overview -> {
                            StudentOverviewContent(student = student)
                        }
                        StudentDetailTab.Workout -> {
                            StudentWorkoutTabContent(
                                studentId = studentId,
                                onNavigateToAssignWorkoutPicker = onNavigateToAssignWorkoutPicker
                            )
                        }
                        StudentDetailTab.Diet -> {
                            StudentDietTabContent(
                                studentId = studentId,
                                onNavigateToAssignDietPicker = onNavigateToAssignDietPicker,
                                onViewDietDetail = onViewDietDetail
                            )
                        }
                        StudentDetailTab.Evolution -> {
                            StudentEvolutionTabContent(studentId = studentId)
                        }
                        StudentDetailTab.Exams, StudentDetailTab.Documents -> {
                            StudentDocumentsTabContent(studentId = studentId)
                        }
                        else -> {
                            StudentSubTabPlaceholder(tab = selectedTab)
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
private fun StudentDetailHeader(
    student: Student,
    onBack: () -> Unit
) {
    val initial = student.user.fullName.trim().firstOrNull()?.uppercase() ?: "A"
    val secondaryInfo = student.goals?.takeIf { it.isNotBlank() }?.let { "Objetivo: $it" } ?: student.user.email

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RitimoIconButton(
                icon = RitimoIcons.ArrowBack,
                contentDescription = "Voltar para lista de alunos",
                onClick = onBack
            )

            Spacer(modifier = Modifier.width(RitimoTheme.spacing.xs))

            RitimoStatusBadge(
                text = "PERFIL DO ALUNO",
                type = BadgeType.PRIMARY
            )
        }

        Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RitimoTheme.shapes.pill)
                    .background(RitimoTheme.colors.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initial,
                    style = RitimoTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                    color = RitimoTheme.colors.primary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.width(RitimoTheme.spacing.md))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.user.fullName,
                    style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                    color = RitimoTheme.colors.onBackground
                )
                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))
                Text(
                    text = secondaryInfo,
                    style = RitimoTheme.typography.bodyMedium,
                    color = RitimoTheme.colors.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun StudentSecondaryTabBar(
    selectedTab: StudentDetailTab,
    onTabSelected: (StudentDetailTab) -> Unit
) {
    val tabs = listOf(
        StudentDetailTab.Overview to "Visão Geral",
        StudentDetailTab.Workout to "Treino",
        StudentDetailTab.Diet to "Dieta",
        StudentDetailTab.Evolution to "Evolução",
        StudentDetailTab.Exams to "Exames",
        StudentDetailTab.Documents to "Documentos",
        StudentDetailTab.Reports to "Relatórios"
    )

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
                    .clickable { onTabSelected(tab) }
                    .padding(horizontal = RitimoTheme.spacing.md, vertical = RitimoTheme.spacing.xs)
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

@Composable
private fun StudentWorkoutTabContent(
    studentId: String,
    onNavigateToAssignWorkoutPicker: () -> Unit,
    viewModel: StudentWorkoutAssignmentViewModel = koinViewModel()
) {
    LaunchedEffect(studentId) {
        viewModel.loadStudentAssignments(studentId)
    }

    val assignedState by viewModel.assignedWorkoutsUiState.collectAsState()

    Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.lg)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Treinos Atribuídos",
                    style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = RitimoTheme.colors.onBackground
                )
                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))
                Text(
                    text = "Planejamento de treino do aluno",
                    style = RitimoTheme.typography.bodyMedium,
                    color = RitimoTheme.colors.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(RitimoTheme.spacing.xs))

            RitimoPrimaryButton(
                text = "+ Atribuir",
                onClick = onNavigateToAssignWorkoutPicker
            )
        }

        when (val state = assignedState) {
            is UiState.Loading -> {
                RitimoLoadingState(
                    message = "Carregando treinos atribuídos...",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is UiState.Error -> {
                RitimoErrorState(
                    title = "Erro ao carregar treinos",
                    message = "Não foi possível carregar os treinos deste aluno.",
                    onRetry = { viewModel.loadStudentAssignments(studentId) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is UiState.Success -> {
                val assignedList = state.data
                if (assignedList.isEmpty()) {
                    RitimoEmptyState(
                        title = "Nenhum treino atribuído",
                        description = "Os treinos atribuídos a este aluno aparecerão aqui.",
                        icon = RitimoIcons.Workouts,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
                        assignedList.forEach { details ->
                            RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(RitimoTheme.spacing.xs),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = details.workout.title,
                                            style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                                            color = RitimoTheme.colors.onSurface
                                        )

                                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs)
                                        ) {
                                            RitimoStatusBadge(
                                                text = "${details.workout.exercises.size} EXERCÍCIOS",
                                                type = BadgeType.PRIMARY
                                            )

                                            if (!details.workout.description.isNullOrBlank()) {
                                                Text(
                                                    text = details.workout.description,
                                                    style = RitimoTheme.typography.bodySmall,
                                                    color = RitimoTheme.colors.onSurfaceVariant,
                                                    maxLines = 1
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(RitimoTheme.spacing.xs))

                                    RitimoIconButton(
                                        icon = RitimoIcons.Trash,
                                        contentDescription = "Remover atribuição do treino",
                                        onClick = {
                                            viewModel.removeAssignment(studentId, details.workout.id)
                                        }
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
}

@Composable
private fun StudentDietTabContent(
    studentId: String,
    onNavigateToAssignDietPicker: () -> Unit,
    onViewDietDetail: (String) -> Unit,
    viewModel: StudentDietAssignmentViewModel = koinViewModel()
) {
    LaunchedEffect(studentId) {
        viewModel.loadStudentAssignments(studentId)
    }

    val assignedState by viewModel.assignedDietsUiState.collectAsState()

    Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.lg)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Plano Alimentar Atribuído",
                    style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = RitimoTheme.colors.onBackground
                )
                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))
                Text(
                    text = "Acompanhamento nutricional do aluno",
                    style = RitimoTheme.typography.bodyMedium,
                    color = RitimoTheme.colors.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(RitimoTheme.spacing.xs))

            RitimoPrimaryButton(
                text = "+ Atribuir",
                onClick = onNavigateToAssignDietPicker
            )
        }

        when (val state = assignedState) {
            is UiState.Loading -> {
                RitimoLoadingState(
                    message = "Carregando plano alimentar...",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is UiState.Error -> {
                RitimoErrorState(
                    title = "Erro ao carregar dieta",
                    message = "Não foi possível carregar o plano alimentar deste aluno.",
                    onRetry = { viewModel.loadStudentAssignments(studentId) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is UiState.Success -> {
                val assignedList = state.data
                if (assignedList.isEmpty()) {
                    RitimoEmptyState(
                        title = "Nenhuma dieta atribuída",
                        description = "Este aluno ainda não possui uma dieta atribuída.",
                        icon = RitimoIcons.Diet,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
                        assignedList.forEach { details ->
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
                                            text = details.diet.title,
                                            style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                            color = RitimoTheme.colors.onSurface
                                        )

                                        RitimoIconButton(
                                            icon = RitimoIcons.Trash,
                                            contentDescription = "Remover atribuição da dieta",
                                            onClick = {
                                                viewModel.removeAssignment(studentId, details.diet.id)
                                            }
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs)
                                    ) {
                                        RitimoStatusBadge(
                                            text = "${details.diet.meals.size} REFEIÇÕES",
                                            type = BadgeType.PRIMARY
                                        )

                                        if (details.diet.totalCalories != null && details.diet.totalCalories > 0) {
                                            RitimoStatusBadge(
                                                text = "${details.diet.totalCalories} KCAL",
                                                type = BadgeType.NEUTRAL
                                            )
                                        }
                                    }

                                    if (!details.diet.description.isNullOrBlank()) {
                                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
                                        Text(
                                            text = details.diet.description,
                                            style = RitimoTheme.typography.bodyMedium,
                                            color = RitimoTheme.colors.onSurfaceVariant
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                                    RitimoPrimaryButton(
                                        text = "Ver dieta",
                                        onClick = { onViewDietDetail(details.diet.id) },
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
}

@Composable
private fun StudentEvolutionTabContent(
    studentId: String,
    viewModel: StudentEvolutionViewModel = koinViewModel()
) {
    LaunchedEffect(studentId) {
        viewModel.loadStudentEvolution(studentId)
    }

    val evolutionState by viewModel.evolutionUiState.collectAsState()
    val completedWorkoutsState by viewModel.completedWorkoutsState.collectAsState()

    when (val state = evolutionState) {
        is UiState.Loading -> {
            RitimoLoadingState(
                message = "Carregando evolução do aluno...",
                modifier = Modifier.fillMaxWidth()
            )
        }
        is UiState.Error -> {
            RitimoErrorState(
                title = "Erro ao carregar evolução",
                message = "Não foi possível carregar a evolução deste aluno.",
                onRetry = { viewModel.loadStudentEvolution(studentId) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        is UiState.Success -> {
            val summary = state.data

            Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.lg)) {
                // 1. Resumo de Peso
                Text(
                    text = "Resumo de Peso",
                    style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = RitimoTheme.colors.onBackground
                )

                RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Peso Atual", style = RitimoTheme.typography.bodySmall, color = RitimoTheme.colors.onSurfaceVariant)
                            Text(text = summary.latestWeightKg?.let { "$it kg" } ?: "—", style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = RitimoTheme.colors.onSurface)
                        }

                        Column {
                            Text(text = "Peso Inicial", style = RitimoTheme.typography.bodySmall, color = RitimoTheme.colors.onSurfaceVariant)
                            Text(text = summary.initialWeightKg?.let { "$it kg" } ?: "—", style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), color = RitimoTheme.colors.onSurface)
                        }

                        Column {
                            Text(text = "Diferença", style = RitimoTheme.typography.bodySmall, color = RitimoTheme.colors.onSurfaceVariant)
                            val diff = summary.weightDifferenceKg
                            val diffStr = when {
                                diff == null -> "—"
                                diff > 0 -> "+$diff kg"
                                else -> "$diff kg"
                            }
                            RitimoStatusBadge(text = diffStr, type = if (diff != null && diff <= 0) BadgeType.PRIMARY else BadgeType.NEUTRAL)
                        }
                    }
                }

                // 2. Histórico de Medidas
                Text(
                    text = "Medidas Corporais (${summary.measurementRecords.size})",
                    style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = RitimoTheme.colors.onBackground
                )

                if (summary.measurementRecords.isEmpty()) {
                    RitimoEmptyState(
                        title = "Sem medidas registradas",
                        description = "Este aluno ainda não possui registros de medidas corporais.",
                        icon = RitimoIcons.Evolution,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
                        summary.measurementRecords.forEach { record ->
                            RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
                                Column {
                                    if (!record.notes.isNullOrBlank()) {
                                        Text(text = record.notes, style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = RitimoTheme.colors.onSurface)
                                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
                                    }
                                    Row(horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs)) {
                                        record.waistCm?.let { RitimoStatusBadge(text = "Cintura: ${it}cm", type = BadgeType.PRIMARY) }
                                        record.abdomenCm?.let { RitimoStatusBadge(text = "Abdómen: ${it}cm", type = BadgeType.NEUTRAL) }
                                        record.chestCm?.let { RitimoStatusBadge(text = "Peito: ${it}cm", type = BadgeType.NEUTRAL) }
                                    }
                                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
                                    Row(horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs)) {
                                        record.armCm?.let { RitimoStatusBadge(text = "Braço: ${it}cm", type = BadgeType.NEUTRAL) }
                                        record.thighCm?.let { RitimoStatusBadge(text = "Coxa: ${it}cm", type = BadgeType.NEUTRAL) }
                                        record.hipCm?.let { RitimoStatusBadge(text = "Anca: ${it}cm", type = BadgeType.NEUTRAL) }
                                    }
                                }
                            }
                        }
                    }
                }

                // 3. Histórico de Treinos Executados
                Text(
                    text = "Histórico de Treinos Executados",
                    style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = RitimoTheme.colors.onBackground
                )

                when (completedWorkoutsState) {
                    is UiState.Success -> {
                        val workoutsList = (completedWorkoutsState as UiState.Success).data
                        if (workoutsList.isEmpty()) {
                            RitimoEmptyState(
                                title = "Nenhum treino concluído",
                                description = "Os treinos concluídos deste aluno aparecerão aqui.",
                                icon = RitimoIcons.Workouts,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
                                workoutsList.forEach { details ->
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
                                                Text(text = details.workout.title, style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = RitimoTheme.colors.onSurface)
                                                Text(text = "Duração: $durStr", style = RitimoTheme.typography.bodyMedium, color = RitimoTheme.colors.primary)
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
        }
        else -> {}
    }
}

@Composable
private fun StudentDocumentsTabContent(
    studentId: String,
    viewModel: StudentDocumentViewModel = koinViewModel()
) {
    LaunchedEffect(studentId) {
        viewModel.loadStudentDocuments(studentId)
    }

    val docsState by viewModel.documentsUiState.collectAsState()

    when (val state = docsState) {
        is UiState.Loading -> {
            RitimoLoadingState(
                message = "Carregando exames e documentos...",
                modifier = Modifier.fillMaxWidth()
            )
        }
        is UiState.Error -> {
            RitimoErrorState(
                title = "Erro ao carregar documentos",
                message = "Não foi possível carregar os exames deste aluno.",
                onRetry = { viewModel.loadStudentDocuments(studentId) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        is UiState.Success -> {
            val docs = state.data

            Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.lg)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Exames e Documentos (${docs.size})",
                            style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = RitimoTheme.colors.onBackground
                        )
                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))
                        Text(
                            text = "Análises clínicas, laudos e avaliações do aluno",
                            style = RitimoTheme.typography.bodyMedium,
                            color = RitimoTheme.colors.onSurfaceVariant
                        )
                    }
                }

                if (docs.isEmpty()) {
                    RitimoEmptyState(
                        title = "Nenhum documento anexado",
                        description = "Os exames e laudos anexados pelo aluno aparecerão aqui.",
                        icon = RitimoIcons.Documents,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
                        docs.forEach { doc ->
                            val sizeMb = (doc.fileSizeBytes.toFloat() / (1024f * 1024f)).toString().take(3)

                            RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = doc.title,
                                            style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = RitimoTheme.colors.onSurface
                                        )

                                        RitimoStatusBadge(
                                            text = doc.category.displayName.uppercase(),
                                            type = BadgeType.PRIMARY
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

                                    Text(
                                        text = "${doc.fileName} • $sizeMb MB (${doc.fileExtension.uppercase()})",
                                        style = RitimoTheme.typography.bodySmall,
                                        color = RitimoTheme.colors.primary
                                    )

                                    if (!doc.notes.isNullOrBlank()) {
                                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
                                        Text(
                                            text = "Obs.: ${doc.notes}",
                                            style = RitimoTheme.typography.bodyMedium,
                                            color = RitimoTheme.colors.onSurfaceVariant
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

                                    RitimoPrimaryButton(
                                        text = "Abrir documento",
                                        onClick = { viewModel.openDocument(doc.fileUri) },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
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
private fun StudentOverviewContent(
    student: Student
) {
    Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.lg)) {

        // 1. Resumo do Aluno (Físico/Objetivo)
        Text(
            text = "Resumo do Aluno",
            style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = RitimoTheme.colors.onBackground
        )

        RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Altura",
                            style = RitimoTheme.typography.bodySmall,
                            color = RitimoTheme.colors.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))
                        Text(
                            text = student.heightCm?.let { "${it.toInt()} cm" } ?: "Não informado",
                            style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = RitimoTheme.colors.onSurface
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Peso Inicial",
                            style = RitimoTheme.typography.bodySmall,
                            color = RitimoTheme.colors.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))
                        Text(
                            text = student.weightKg?.let { "$it kg" } ?: "Não informado",
                            style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = RitimoTheme.colors.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

                Text(
                    text = "Objetivo Principal",
                    style = RitimoTheme.typography.bodySmall,
                    color = RitimoTheme.colors.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))
                Text(
                    text = student.goals?.takeIf { it.isNotBlank() } ?: "Não informado",
                    style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = RitimoTheme.colors.onSurface
                )
            }
        }

        // 2. Treino Atual Section
        Text(
            text = "Treino Atual",
            style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = RitimoTheme.colors.onBackground
        )

        RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RitimoTheme.shapes.medium)
                        .background(RitimoTheme.colors.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = RitimoIcons.Workouts,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = RitimoTheme.colors.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(RitimoTheme.spacing.md))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Nenhum treino atribuído",
                        style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = RitimoTheme.colors.onSurface
                    )
                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))
                    Text(
                        text = "Os treinos prescritos para este aluno aparecerão nesta seção.",
                        style = RitimoTheme.typography.bodyMedium,
                        color = RitimoTheme.colors.onSurfaceVariant
                    )
                }
            }
        }

        // 3. Dieta Atual Section
        Text(
            text = "Dieta Atual",
            style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = RitimoTheme.colors.onBackground
        )

        RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RitimoTheme.shapes.medium)
                        .background(RitimoTheme.colors.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = RitimoIcons.Diet,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = RitimoTheme.colors.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(RitimoTheme.spacing.md))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Nenhuma dieta atribuída",
                        style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = RitimoTheme.colors.onSurface
                    )
                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))
                    Text(
                        text = "O plano alimentar do aluno aparecerá aqui quando for prescrito.",
                        style = RitimoTheme.typography.bodyMedium,
                        color = RitimoTheme.colors.onSurfaceVariant
                    )
                }
            }
        }

        // 4. Progresso / Evolução Section
        Text(
            text = "Evolução Corporal",
            style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = RitimoTheme.colors.onBackground
        )

        RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RitimoTheme.shapes.medium)
                        .background(RitimoTheme.colors.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = RitimoIcons.Evolution,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = RitimoTheme.colors.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(RitimoTheme.spacing.md))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Sem registros de evolução",
                        style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = RitimoTheme.colors.onSurface
                    )
                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))
                    Text(
                        text = "Os registros de peso, fotos e avaliações físicas aparecerão aqui.",
                        style = RitimoTheme.typography.bodyMedium,
                        color = RitimoTheme.colors.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun StudentSubTabPlaceholder(
    tab: StudentDetailTab
) {
    val tabName = when (tab) {
        StudentDetailTab.Overview -> "Visão Geral"
        StudentDetailTab.Workout -> "Treino"
        StudentDetailTab.Diet -> "Dieta"
        StudentDetailTab.Evolution -> "Evolução"
        StudentDetailTab.Exams -> "Exames"
        StudentDetailTab.Documents -> "Documentos"
        StudentDetailTab.Reports -> "Relatórios"
    }

    RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RitimoTheme.spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Seção $tabName",
                style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = RitimoTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
            Text(
                text = "A funcionalidade completa da seção $tabName será desenvolvida em uma próxima etapa.",
                style = RitimoTheme.typography.bodyMedium,
                color = RitimoTheme.colors.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
