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
import com.ranielschneider.ritimofit.core.design.background.RitimoAmbientBackground
import com.ranielschneider.ritimofit.core.design.components.button.RitimoIconButton
import com.ranielschneider.ritimofit.core.design.components.button.RitimoPrimaryButton
import com.ranielschneider.ritimofit.core.design.components.button.RitimoTertiaryButton
import com.ranielschneider.ritimofit.core.design.components.card.RitimoGlassCard
import com.ranielschneider.ritimofit.core.design.components.indicator.BadgeType
import com.ranielschneider.ritimofit.core.design.components.indicator.RitimoStatusBadge
import com.ranielschneider.ritimofit.core.design.components.state.RitimoEmptyState
import com.ranielschneider.ritimofit.core.design.components.state.RitimoErrorState
import com.ranielschneider.ritimofit.core.design.components.state.RitimoLoadingState
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme
import com.ranielschneider.ritimofit.domain.model.Diet
import com.ranielschneider.ritimofit.presentation.base.UiState
import com.ranielschneider.ritimofit.presentation.viewmodel.DietViewModel
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentDietAssignmentViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AssignDietPickerScreen(
    studentId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    dietViewModel: DietViewModel = koinViewModel(),
    assignmentViewModel: StudentDietAssignmentViewModel = koinViewModel()
) {
    LaunchedEffect(studentId) {
        dietViewModel.loadDiets()
        assignmentViewModel.loadStudentAssignments(studentId)
    }

    val dietsState by dietViewModel.dietsUiState.collectAsState()
    val assignedState by assignmentViewModel.assignedDietsUiState.collectAsState()

    val assignedDietIds = if (assignedState is UiState.Success) {
        (assignedState as UiState.Success).data.map { it.diet.id }.toSet()
    } else {
        emptySet()
    }

    var pendingDietToAssign by remember { mutableStateOf<Diet?>(null) }

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
                    text = "ATRIBUIR DIETA",
                    type = BadgeType.PRIMARY
                )
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

            Text(
                text = "Biblioteca de Dietas",
                style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                color = RitimoTheme.colors.onBackground
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

            Text(
                text = "Selecione um plano alimentar da sua biblioteca para atribuir a este aluno",
                style = RitimoTheme.typography.bodyMedium,
                color = RitimoTheme.colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

            // 2. Library Diets List
            when (val state = dietsState) {
                is UiState.Loading -> {
                    RitimoLoadingState(
                        message = "Carregando biblioteca de dietas...",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is UiState.Error -> {
                    RitimoErrorState(
                        title = "Erro ao carregar dietas",
                        message = "Não foi possível carregar as dietas da biblioteca.",
                        onRetry = { dietViewModel.loadDiets() },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is UiState.Success -> {
                    val diets = state.data
                    if (diets.isEmpty()) {
                        RitimoEmptyState(
                            title = "Sua biblioteca está vazia",
                            description = "Crie planos alimentares na área 'Planejar → Dietas' antes de atribuir a um aluno.",
                            icon = RitimoIcons.Diet,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = diets,
                                key = { diet -> diet.id }
                            ) { diet ->
                                val isAssigned = assignedDietIds.contains(diet.id)

                                AssignableDietCard(
                                    diet = diet,
                                    isAlreadyAssigned = isAssigned,
                                    onAssign = {
                                        if (assignedDietIds.isNotEmpty()) {
                                            pendingDietToAssign = diet
                                        } else {
                                            assignmentViewModel.assignDiet(
                                                studentId = studentId,
                                                dietId = diet.id,
                                                onSuccess = onBack
                                            )
                                        }
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

        // Replacement Confirmation Dialog
        pendingDietToAssign?.let { newDiet ->
            AlertDialog(
                onDismissRequest = { pendingDietToAssign = null },
                title = {
                    Text(
                        text = "Substituir dieta ativa?",
                        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                text = {
                    Text(
                        text = "Este aluno já possui uma dieta ativa. Deseja substituir a dieta atual pelo novo plano '${newDiet.title}'?",
                        style = RitimoTheme.typography.bodyMedium,
                        color = RitimoTheme.colors.onSurfaceVariant
                    )
                },
                confirmButton = {
                    RitimoPrimaryButton(
                        text = "Substituir",
                        onClick = {
                            assignmentViewModel.replaceDietAssignment(
                                studentId = studentId,
                                newDietId = newDiet.id,
                                onSuccess = onBack
                            )
                            pendingDietToAssign = null
                        }
                    )
                },
                dismissButton = {
                    RitimoTertiaryButton(
                        text = "Cancelar",
                        onClick = { pendingDietToAssign = null }
                    )
                }
            )
        }
    }
}

@Composable
private fun AssignableDietCard(
    diet: Diet,
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
                    text = diet.title,
                    style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = RitimoTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs)
                ) {
                    RitimoStatusBadge(
                        text = "${diet.meals.size} REFEIÇÕES",
                        type = BadgeType.PRIMARY
                    )

                    if (diet.totalCalories != null && diet.totalCalories > 0) {
                        RitimoStatusBadge(
                            text = "${diet.totalCalories} KCAL",
                            type = BadgeType.NEUTRAL
                        )
                    }
                }

                if (!diet.description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))
                    Text(
                        text = diet.description,
                        style = RitimoTheme.typography.bodySmall,
                        color = RitimoTheme.colors.onSurfaceVariant,
                        maxLines = 1
                    )
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
