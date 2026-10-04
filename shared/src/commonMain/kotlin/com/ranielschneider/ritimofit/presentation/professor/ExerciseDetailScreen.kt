package com.ranielschneider.ritimofit.presentation.professor

import androidx.compose.foundation.background
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
import com.ranielschneider.ritimofit.core.design.components.card.RitimoGlassCard
import com.ranielschneider.ritimofit.core.design.components.indicator.BadgeType
import com.ranielschneider.ritimofit.core.design.components.indicator.RitimoStatusBadge
import com.ranielschneider.ritimofit.core.design.components.state.RitimoErrorState
import com.ranielschneider.ritimofit.core.design.components.state.RitimoLoadingState
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme
import com.ranielschneider.ritimofit.domain.model.Exercise
import com.ranielschneider.ritimofit.presentation.base.UiState
import com.ranielschneider.ritimofit.presentation.viewmodel.ExerciseViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ExerciseDetailScreen(
    exerciseId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExerciseViewModel = koinViewModel()
) {
    LaunchedEffect(exerciseId) {
        viewModel.loadExerciseDetail(exerciseId)
    }

    val detailState by viewModel.exerciseDetailState.collectAsState()
    val scrollState = rememberScrollState()

    RitimoAmbientBackground(modifier = modifier.fillMaxSize()) {
        when (val state = detailState) {
            is UiState.Loading -> {
                RitimoLoadingState(
                    message = "Carregando detalhes do exercício...",
                    modifier = Modifier.fillMaxSize()
                )
            }
            is UiState.Error -> {
                RitimoErrorState(
                    title = "Exercício não encontrado",
                    message = "Não foi possível carregar as informações deste exercício.",
                    onRetry = { viewModel.loadExerciseDetail(exerciseId) },
                    modifier = Modifier.fillMaxSize()
                )
            }
            is UiState.Success -> {
                val exercise = state.data

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
                            contentDescription = "Voltar para lista de exercícios",
                            onClick = onBack
                        )

                        Spacer(modifier = Modifier.width(RitimoTheme.spacing.xs))

                        RitimoStatusBadge(
                            text = "DETALHE DO EXERCÍCIO",
                            type = BadgeType.PRIMARY
                        )
                    }

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

                    Text(
                        text = exercise.name,
                        style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                        color = RitimoTheme.colors.onBackground
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RitimoStatusBadge(
                            text = exercise.muscleGroup.uppercase(),
                            type = BadgeType.PRIMARY
                        )

                        if (exercise.equipment != null) {
                            RitimoStatusBadge(
                                text = exercise.equipment.uppercase(),
                                type = BadgeType.NEUTRAL
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

                    // 2. Demonstration / Media Area Placeholder
                    RitimoGlassCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(RitimoTheme.spacing.md),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RitimoTheme.shapes.medium)
                                    .background(RitimoTheme.colors.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = RitimoIcons.Play,
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp),
                                    tint = RitimoTheme.colors.primary
                                )
                            }

                            Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

                            Text(
                                text = "Demonstração Visual",
                                style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = RitimoTheme.colors.onSurface
                            )

                            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

                            Text(
                                text = "Animação / GIF interativo de execução será carregado futuramente via API remota.",
                                style = RitimoTheme.typography.bodyMedium,
                                color = RitimoTheme.colors.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

                    // 3. Descrição
                    if (exercise.description != null) {
                        Text(
                            text = "Descrição",
                            style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = RitimoTheme.colors.onBackground
                        )

                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

                        RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = exercise.description,
                                style = RitimoTheme.typography.bodyMedium,
                                color = RitimoTheme.colors.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))
                    }

                    // 4. Instruções de Execução
                    Text(
                        text = "Instruções de Execução",
                        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = RitimoTheme.colors.onBackground
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

                    RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
                        if (exercise.instructions.isNotEmpty()) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)
                            ) {
                                exercise.instructions.forEachIndexed { index, step ->
                                    Row(
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(RitimoTheme.shapes.pill)
                                                .background(RitimoTheme.colors.primaryContainer),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${index + 1}",
                                                style = RitimoTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = RitimoTheme.colors.primary
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(RitimoTheme.spacing.md))

                                        Text(
                                            text = step,
                                            style = RitimoTheme.typography.bodyMedium,
                                            color = RitimoTheme.colors.onSurface,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = "Sem instruções passo a passo cadastradas.",
                                style = RitimoTheme.typography.bodyMedium,
                                color = RitimoTheme.colors.onSurfaceVariant
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
