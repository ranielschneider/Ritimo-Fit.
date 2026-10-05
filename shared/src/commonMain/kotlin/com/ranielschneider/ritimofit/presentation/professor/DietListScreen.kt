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
import com.ranielschneider.ritimofit.domain.model.Diet
import com.ranielschneider.ritimofit.presentation.base.UiState
import com.ranielschneider.ritimofit.presentation.viewmodel.DietViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DietListScreen(
    onCreateDiet: () -> Unit,
    onEditDiet: (String) -> Unit,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DietViewModel = koinViewModel()
) {
    val dietsState by viewModel.dietsUiState.collectAsState()

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
                text = "Dietas",
                style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                color = RitimoTheme.colors.onBackground
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

            Text(
                text = "Elabore e organize seus planos alimentares",
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
                    val isSelected = tab == "Dietas"
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
                text = "Criar plano alimentar",
                onClick = onCreateDiet,
                icon = RitimoIcons.Add,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

            // 4. Content Section
            when (val state = dietsState) {
                is UiState.Loading -> {
                    RitimoLoadingState(
                        message = "Carregando seus planos alimentares...",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is UiState.Error -> {
                    RitimoErrorState(
                        title = "Erro ao carregar dietas",
                        message = "Não foi possível carregar suas dietas salvas.",
                        onRetry = { viewModel.loadDiets() },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is UiState.Success -> {
                    val diets = state.data
                    if (diets.isEmpty()) {
                        RitimoEmptyState(
                            title = "Nenhuma dieta criada",
                            description = "Crie seu primeiro plano alimentar para começar a montar seus planejamentos.",
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
                                DietCard(
                                    diet = diet,
                                    onClick = { onEditDiet(diet.id) },
                                    onDelete = { viewModel.deleteDiet(diet.id) },
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
private fun DietCard(
    diet: Diet,
    onClick: () -> Unit,
    onDelete: () -> Unit,
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
                    imageVector = RitimoIcons.Diet,
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

            Spacer(modifier = Modifier.width(RitimoTheme.spacing.xs))

            RitimoIconButton(
                icon = RitimoIcons.Trash,
                contentDescription = "Excluir plano alimentar",
                onClick = onDelete
            )
        }
    }
}
