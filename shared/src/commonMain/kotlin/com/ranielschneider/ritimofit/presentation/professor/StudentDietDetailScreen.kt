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
import com.ranielschneider.ritimofit.core.design.components.state.RitimoLoadingState
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme
import com.ranielschneider.ritimofit.domain.model.Meal
import com.ranielschneider.ritimofit.presentation.viewmodel.DietViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StudentDietDetailScreen(
    dietId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DietViewModel = koinViewModel()
) {
    LaunchedEffect(dietId) {
        viewModel.loadDietForEditing(dietId)
    }

    val title by viewModel.dietTitle.collectAsState()
    val description by viewModel.dietDescription.collectAsState()
    val totalCalories by viewModel.totalCalories.collectAsState()
    val proteinGrams by viewModel.proteinGrams.collectAsState()
    val carbsGrams by viewModel.carbsGrams.collectAsState()
    val fatsGrams by viewModel.fatsGrams.collectAsState()
    val meals by viewModel.meals.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    val scrollState = rememberScrollState()

    RitimoAmbientBackground(modifier = modifier.fillMaxSize()) {
        if (isSaving) {
            RitimoLoadingState(
                message = "Carregando plano alimentar...",
                modifier = Modifier.fillMaxSize()
            )
        } else {
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
                        contentDescription = "Voltar ao perfil do aluno",
                        onClick = onBack
                    )

                    Spacer(modifier = Modifier.width(RitimoTheme.spacing.xs))

                    RitimoStatusBadge(
                        text = "PLANO ALIMENTAR ATRIBUÍDO",
                        type = BadgeType.PRIMARY
                    )
                }

                Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

                Text(
                    text = title.ifBlank { "Plano Alimentar" },
                    style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                    color = RitimoTheme.colors.onBackground
                )

                if (description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))
                    Text(
                        text = description,
                        style = RitimoTheme.typography.bodyMedium,
                        color = RitimoTheme.colors.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                // 2. Macros Badge Summary Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (totalCalories.isNotBlank()) {
                        RitimoStatusBadge(text = "$totalCalories KCAL", type = BadgeType.PRIMARY)
                    }
                    if (proteinGrams.isNotBlank()) {
                        RitimoStatusBadge(text = "${proteinGrams}g PROTEÍNA", type = BadgeType.NEUTRAL)
                    }
                    if (carbsGrams.isNotBlank()) {
                        RitimoStatusBadge(text = "${carbsGrams}g CARBS", type = BadgeType.NEUTRAL)
                    }
                    if (fatsGrams.isNotBlank()) {
                        RitimoStatusBadge(text = "${fatsGrams}g GORDURA", type = BadgeType.NEUTRAL)
                    }
                }

                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

                // 3. Meals Section Header
                Text(
                    text = "Refeições (${meals.size})",
                    style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = RitimoTheme.colors.onBackground
                )

                Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

                // 4. Meals Read-Only Cards
                Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
                    meals.forEach { meal ->
                        ReadOnlyMealCard(meal = meal, modifier = Modifier.fillMaxWidth())
                    }
                }

                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxl))
            }
        }
    }
}

@Composable
private fun ReadOnlyMealCard(
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
