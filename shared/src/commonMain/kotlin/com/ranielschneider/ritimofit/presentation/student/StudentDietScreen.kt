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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ranielschneider.ritimofit.core.design.background.RitimoAmbientBackground
import com.ranielschneider.ritimofit.core.design.components.card.RitimoGlassCard
import com.ranielschneider.ritimofit.core.design.components.indicator.BadgeType
import com.ranielschneider.ritimofit.core.design.components.indicator.RitimoStatusBadge
import com.ranielschneider.ritimofit.core.design.components.state.RitimoEmptyState
import com.ranielschneider.ritimofit.core.design.components.state.RitimoErrorState
import com.ranielschneider.ritimofit.core.design.components.state.RitimoLoadingState
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme
import com.ranielschneider.ritimofit.domain.model.Meal
import com.ranielschneider.ritimofit.presentation.base.UiState
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentDietViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StudentDietScreen(
    modifier: Modifier = Modifier,
    viewModel: StudentDietViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    RitimoAmbientBackground(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is UiState.Loading -> {
                RitimoLoadingState(
                    message = "Carregando seu plano alimentar...",
                    modifier = Modifier.fillMaxSize()
                )
            }
            is UiState.Error -> {
                RitimoErrorState(
                    title = "Erro ao carregar dieta",
                    message = "Não foi possível carregar seu plano alimentar.",
                    onRetry = { viewModel.loadStudentDiet() },
                    modifier = Modifier.fillMaxSize()
                )
            }
            is UiState.Success -> {
                val diet = state.data

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
                        text = "Minha Dieta",
                        style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                        color = RitimoTheme.colors.onBackground
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

                    Text(
                        text = "Acompanhe seu plano alimentar e orientação nutricional",
                        style = RitimoTheme.typography.bodyMedium,
                        color = RitimoTheme.colors.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                    if (diet == null) {
                        RitimoEmptyState(
                            title = "Você ainda não possui uma dieta ativa",
                            description = "Quando seu professor atribuir um plano alimentar, ele aparecerá aqui.",
                            icon = RitimoIcons.Diet,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        // Diet Title & Description Card
                        RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
                            Column {
                                Text(
                                    text = diet.title,
                                    style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = RitimoTheme.colors.onSurface
                                )

                                if (!diet.description.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
                                    Text(
                                        text = diet.description,
                                        style = RitimoTheme.typography.bodyMedium,
                                        color = RitimoTheme.colors.onSurfaceVariant
                                    )
                                }

                                // Macros Badges
                                if (diet.totalCalories != null || diet.proteinGrams != null || diet.carbsGrams != null || diet.fatsGrams != null) {
                                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (diet.totalCalories != null && diet.totalCalories > 0) {
                                            RitimoStatusBadge(
                                                text = "${diet.totalCalories} KCAL",
                                                type = BadgeType.PRIMARY
                                            )
                                        }
                                        if (diet.proteinGrams != null && diet.proteinGrams > 0) {
                                            RitimoStatusBadge(
                                                text = "${diet.proteinGrams}g PROTEÍNA",
                                                type = BadgeType.NEUTRAL
                                            )
                                        }
                                        if (diet.carbsGrams != null && diet.carbsGrams > 0) {
                                            RitimoStatusBadge(
                                                text = "${diet.carbsGrams}g CARBS",
                                                type = BadgeType.NEUTRAL
                                            )
                                        }
                                        if (diet.fatsGrams != null && diet.fatsGrams > 0) {
                                            RitimoStatusBadge(
                                                text = "${diet.fatsGrams}g GORDURA",
                                                type = BadgeType.NEUTRAL
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

                        // Meals Header
                        Text(
                            text = "Refeições (${diet.meals.size})",
                            style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = RitimoTheme.colors.onBackground
                        )

                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

                        // Meals List
                        Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
                            diet.meals.forEach { meal ->
                                StudentMealCard(meal = meal, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(120.dp))
                }
            }
            else -> {}
        }
    }
}

@Composable
private fun StudentMealCard(
    meal: Meal,
    modifier: Modifier = Modifier
) {
    RitimoGlassCard(modifier = modifier) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = meal.name,
                    style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = RitimoTheme.colors.onSurface
                )

                if (!meal.time.isNullOrBlank()) {
                    RitimoStatusBadge(
                        text = meal.time,
                        type = BadgeType.NEUTRAL
                    )
                }
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

            if (meal.foods.isEmpty()) {
                Text(
                    text = "Nenhum alimento cadastrado nesta refeição.",
                    style = RitimoTheme.typography.bodyMedium,
                    color = RitimoTheme.colors.onSurfaceVariant
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs)) {
                    meal.foods.forEach { food ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = RitimoTheme.spacing.xxs),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = food.name,
                                    style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = RitimoTheme.colors.onSurface
                                )
                                Text(
                                    text = "${food.amount} ${food.unit}" + (food.notes?.let { " • $it" } ?: ""),
                                    style = RitimoTheme.typography.bodyMedium,
                                    color = RitimoTheme.colors.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
