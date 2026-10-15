package com.ranielschneider.ritimofit.presentation.student

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.ranielschneider.ritimofit.core.design.components.button.RitimoSecondaryButton
import com.ranielschneider.ritimofit.core.design.components.card.RitimoGlassCard
import com.ranielschneider.ritimofit.core.design.components.indicator.BadgeType
import com.ranielschneider.ritimofit.core.design.components.indicator.RitimoStatusBadge
import com.ranielschneider.ritimofit.core.design.components.indicator.RitimoWaterBottle
import com.ranielschneider.ritimofit.core.design.components.state.RitimoEmptyState
import com.ranielschneider.ritimofit.core.design.components.state.RitimoErrorState
import com.ranielschneider.ritimofit.core.design.components.state.RitimoLoadingState
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme
import com.ranielschneider.ritimofit.domain.calculator.NutritionalCalculator
import com.ranielschneider.ritimofit.domain.model.User
import com.ranielschneider.ritimofit.domain.model.WaterTracking
import com.ranielschneider.ritimofit.presentation.base.UiState
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentTodayData
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentTodayViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StudentTodayScreen(
    onViewWorkout: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StudentTodayViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    RitimoAmbientBackground(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is UiState.Loading -> {
                RitimoLoadingState(
                    message = "Carregando sua rotina...",
                    modifier = Modifier.fillMaxSize()
                )
            }
            is UiState.Error -> {
                RitimoErrorState(
                    title = "Erro ao carregar",
                    message = "Não foi possível carregar sua rotina de hoje.",
                    onRetry = { viewModel.loadTodayContent() },
                    modifier = Modifier.fillMaxSize()
                )
            }
            is UiState.Success -> {
                val data = state.data

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = RitimoTheme.spacing.lg)
                ) {
                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

                    // 1. Header Section
                    StudentTodayHeader(studentUser = data.studentUser)

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                    // 2. Daily Progress Section
                    DailyProgressSection(data = data)

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                    // 3. Treinos de Hoje
                    TodayWorkoutsSection(
                        data = data,
                        onViewWorkout = onViewWorkout,
                        onCompleteWorkout = viewModel::completeWorkout
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                    // 4. Hidratação com Garrafa Interativa
                    HydrationSection(
                        water = data.waterTracking,
                        onAddWater = viewModel::addWater,
                        onRemoveWater = viewModel::removeWater
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                    // 5. Alimentação da Dieta Ativa com Conclusão de Refeições
                    NutritionSection(
                        data = data,
                        onCompleteMeal = viewModel::completeMeal,
                        onUndoMealCompletion = viewModel::undoMealCompletion
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                    // 6. Evolução
                    EvolutionSection(completionsCount = data.completions.size)

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                    // 7. Ritmo / Constância
                    RhythmSection(completionsCount = data.completions.size)

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                    // 8. Mensagem do Professor
                    ProfessorMessageSection()

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                    // 9. Resumo Final do Dia
                    DailySummarySection(data = data)

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxl))
                }
            }
            else -> {}
        }
    }
}

@Composable
private fun StudentTodayHeader(studentUser: User) {
    val name = studentUser.fullName.split(" ").firstOrNull() ?: studentUser.fullName

    Column(modifier = Modifier.fillMaxWidth()) {
        RitimoStatusBadge(
            text = "ÁREA DO ALUNO",
            type = BadgeType.PRIMARY
        )

        Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

        Text(
            text = "Bom dia, $name 👋",
            style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
            color = RitimoTheme.colors.onBackground
        )

        Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

        Text(
            text = "Vamos manter o ritmo hoje.",
            style = RitimoTheme.typography.bodyMedium,
            color = RitimoTheme.colors.onSurfaceVariant
        )
    }
}

@Composable
private fun DailyProgressSection(data: StudentTodayData) {
    val totalWorkouts = data.assignedWorkouts.size
    val completedWorkouts = data.completions.size.coerceAtMost(totalWorkouts)

    val activeDiet = data.activeDiet
    val totalMeals = activeDiet?.meals?.size ?: 0
    val completedMeals = if (activeDiet != null) {
        data.mealCompletions.count { it.dietId == activeDiet.id }
    } else 0

    val waterGoalReached = data.waterTracking.consumedMl >= data.waterTracking.dailyGoalMl

    var totalGoals = 0
    var completedGoals = 0

    if (totalWorkouts > 0) {
        totalGoals += totalWorkouts
        completedGoals += completedWorkouts
    }

    if (totalMeals > 0) {
        totalGoals += totalMeals
        completedGoals += completedMeals
    }

    totalGoals += 1
    if (waterGoalReached) {
        completedGoals += 1
    }

    val progressFraction = if (totalGoals > 0) completedGoals.toFloat() / totalGoals.toFloat() else 0f
    val progressPercent = (progressFraction * 100).toInt()

    Text(
        text = "SEU DIA",
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
                Text(
                    text = "$completedGoals de $totalGoals metas concluídas",
                    style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = RitimoTheme.colors.onSurface
                )

                RitimoStatusBadge(
                    text = "$progressPercent%",
                    type = if (progressPercent == 100) BadgeType.PRIMARY else BadgeType.NEUTRAL
                )
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

            // Custom Lime Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RitimoTheme.shapes.pill)
                    .background(RitimoTheme.colors.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progressFraction.coerceIn(0.01f, 1f))
                        .height(8.dp)
                        .clip(RitimoTheme.shapes.pill)
                        .background(RitimoTheme.colors.primary)
                )
            }
        }
    }
}

@Composable
private fun TodayWorkoutsSection(
    data: StudentTodayData,
    onViewWorkout: (String) -> Unit,
    onCompleteWorkout: (String) -> Unit
) {
    Text(
        text = "TREINOS DE HOJE",
        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = RitimoTheme.colors.onBackground
    )

    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

    if (data.assignedWorkouts.isNotEmpty()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md),
            modifier = Modifier.fillMaxWidth()
        ) {
            data.assignedWorkouts.forEach { workout ->
                val isCompletedToday = data.completions.any { it.workoutId == workout.id }

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
                                text = workout.title,
                                style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = RitimoTheme.colors.onSurface
                            )

                            if (isCompletedToday) {
                                RitimoStatusBadge(
                                    text = "CONCLUÍDO HOJE",
                                    type = BadgeType.PRIMARY
                                )
                            } else {
                                RitimoStatusBadge(
                                    text = "${workout.exercises.size} exercícios",
                                    type = BadgeType.NEUTRAL
                                )
                            }
                        }

                        if (!workout.description.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
                            Text(
                                text = workout.description,
                                style = RitimoTheme.typography.bodyMedium,
                                color = RitimoTheme.colors.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.sm)
                        ) {
                            RitimoPrimaryButton(
                                text = "Ver treino",
                                onClick = { onViewWorkout(workout.id) },
                                modifier = Modifier.weight(1f)
                            )

                            if (!isCompletedToday) {
                                RitimoSecondaryButton(
                                    text = "✓ Marcar concluído",
                                    onClick = { onCompleteWorkout(workout.id) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    } else {
        RitimoEmptyState(
            title = "Nenhum treino para hoje",
            description = "Seu professor ainda não atribuiu um treino.",
            icon = RitimoIcons.Workouts,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun HydrationSection(
    water: WaterTracking,
    onAddWater: (Int) -> Unit,
    onRemoveWater: (Int) -> Unit
) {
    val consumedLiters = (water.consumedMl.toFloat() / 1000f)
    val goalLiters = (water.dailyGoalMl.toFloat() / 1000f)
    val remainingMl = (water.dailyGoalMl - water.consumedMl).coerceAtLeast(0)
    val exceededMl = (water.consumedMl - water.dailyGoalMl).coerceAtLeast(0)

    val consumedStr = if (consumedLiters % 1f == 0f) "${consumedLiters.toInt()}" else consumedLiters.toString().take(3).replace(".", ",")
    val goalStr = if (goalLiters % 1f == 0f) "${goalLiters.toInt()}" else goalLiters.toString().take(3).replace(".", ",")

    Text(
        text = "HIDRATAÇÃO",
        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = RitimoTheme.colors.onBackground
    )

    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

    RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RitimoTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Interactive Vector Water Bottle (shows remaining water level)
            RitimoWaterBottle(
                consumedMl = water.consumedMl,
                dailyGoalMl = water.dailyGoalMl,
                size = 110.dp
            )

            Spacer(modifier = Modifier.width(RitimoTheme.spacing.md))

            // Hydro Info & Controls
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$consumedStr / $goalStr L",
                    style = RitimoTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                    color = RitimoTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

                if (exceededMl > 0) {
                    Text(
                        text = "Meta atingida! 🎉 ($exceededMl ml acima)",
                        style = RitimoTheme.typography.bodyMedium,
                        color = RitimoTheme.colors.primary
                    )
                } else if (remainingMl == 0) {
                    Text(
                        text = "Meta de hidratação atingida! 🎉",
                        style = RitimoTheme.typography.bodyMedium,
                        color = RitimoTheme.colors.primary
                    )
                } else {
                    Text(
                        text = "$remainingMl ml restantes para meta",
                        style = RitimoTheme.typography.bodyMedium,
                        color = RitimoTheme.colors.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

                // Quick Add/Remove Water Controls
                Row(
                    horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RitimoSecondaryButton(
                        text = "+250 ml",
                        onClick = { onAddWater(250) }
                    )

                    RitimoSecondaryButton(
                        text = "+500 ml",
                        onClick = { onAddWater(500) }
                    )

                    if (water.consumedMl > 0) {
                        RitimoIconButton(
                            icon = RitimoIcons.Clear,
                            contentDescription = "Reduzir 250ml",
                            onClick = { onRemoveWater(250) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NutritionSection(
    data: StudentTodayData,
    onCompleteMeal: (dietId: String, mealId: String) -> Unit,
    onUndoMealCompletion: (dietId: String, mealId: String) -> Unit
) {
    val activeDiet = data.activeDiet

    Text(
        text = "ALIMENTAÇÃO DE HOJE",
        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = RitimoTheme.colors.onBackground
    )

    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

    if (activeDiet != null) {
        val completedMealsList = activeDiet.meals.filter { meal ->
            data.mealCompletions.any { it.dietId == activeDiet.id && it.mealId == meal.id }
        }
        val consumedSummary = NutritionalCalculator.sumDietMacros(completedMealsList)

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
                        text = activeDiet.title,
                        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = RitimoTheme.colors.onSurface
                    )

                    RitimoStatusBadge(
                        text = "${activeDiet.meals.size} REFEIÇÕES",
                        type = BadgeType.PRIMARY
                    )
                }

                if (!activeDiet.description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
                    Text(
                        text = activeDiet.description,
                        style = RitimoTheme.typography.bodyMedium,
                        color = RitimoTheme.colors.onSurfaceVariant
                    )
                }

                // Macros summary badges (Target vs Consumed)
                if (activeDiet.totalCalories != null || activeDiet.proteinGrams != null || activeDiet.carbsGrams != null || activeDiet.fatsGrams != null) {
                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (activeDiet.totalCalories != null && activeDiet.totalCalories > 0) {
                            RitimoStatusBadge(text = "${activeDiet.totalCalories} KCAL", type = BadgeType.PRIMARY)
                        }
                        if (activeDiet.proteinGrams != null && activeDiet.proteinGrams > 0) {
                            RitimoStatusBadge(text = "${activeDiet.proteinGrams}g PROTEÍNA", type = BadgeType.NEUTRAL)
                        }
                        if (activeDiet.carbsGrams != null && activeDiet.carbsGrams > 0) {
                            RitimoStatusBadge(text = "${activeDiet.carbsGrams}g CARBS", type = BadgeType.NEUTRAL)
                        }
                        if (activeDiet.fatsGrams != null && activeDiet.fatsGrams > 0) {
                            RitimoStatusBadge(text = "${activeDiet.fatsGrams}g GORDURA", type = BadgeType.NEUTRAL)
                        }
                    }
                }

                if (completedMealsList.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
                    RitimoStatusBadge(
                        text = "CONSUMIDO HOJE: ${consumedSummary.totalCaloriesKcal} / ${activeDiet.totalCalories ?: 0} KCAL • ${consumedSummary.proteinGrams}g P • ${consumedSummary.carbsGrams}g C • ${consumedSummary.fatGrams}g G",
                        type = BadgeType.PRIMARY
                    )
                }

                Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

                // Render meals, food items, and completion buttons
                Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
                    activeDiet.meals.forEach { meal ->
                        val isMealCompleted = data.mealCompletions.any { it.dietId == activeDiet.id && it.mealId == meal.id }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RitimoTheme.shapes.medium)
                                .background(RitimoTheme.colors.surfaceVariant.copy(alpha = 0.5f))
                                .padding(RitimoTheme.spacing.sm)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = meal.name,
                                    style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = RitimoTheme.colors.onSurface
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (!meal.time.isNullOrBlank()) {
                                        RitimoStatusBadge(text = meal.time, type = BadgeType.NEUTRAL)
                                    }

                                    RitimoStatusBadge(
                                        text = if (isMealCompleted) "CONCLUÍDA" else "PENDENTE",
                                        type = if (isMealCompleted) BadgeType.PRIMARY else BadgeType.NEUTRAL
                                    )
                                }
                            }

                            if (meal.foods.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
                                Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xxs)) {
                                    meal.foods.forEach { food ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "• ${food.name}",
                                                style = RitimoTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                                color = RitimoTheme.colors.onSurface
                                            )
                                            Text(
                                                text = "${food.amount} ${food.unit}",
                                                style = RitimoTheme.typography.bodyMedium,
                                                color = RitimoTheme.colors.primary
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(RitimoTheme.spacing.sm))

                            if (isMealCompleted) {
                                RitimoSecondaryButton(
                                    text = "Desfazer conclusão",
                                    onClick = { onUndoMealCompletion(activeDiet.id, meal.id) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else {
                                RitimoSecondaryButton(
                                    text = "✓ Marcar como concluída",
                                    onClick = { onCompleteMeal(activeDiet.id, meal.id) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    } else {
        RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(RitimoTheme.spacing.xs)
            ) {
                Text(
                    text = "Sua alimentação ainda não foi configurada",
                    style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = RitimoTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))
                Text(
                    text = "Seu professor ainda não cadastrou seu plano alimentar.",
                    style = RitimoTheme.typography.bodyMedium,
                    color = RitimoTheme.colors.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun EvolutionSection(completionsCount: Int) {
    Text(
        text = "SUA EVOLUÇÃO",
        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = RitimoTheme.colors.onBackground
    )

    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

    RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RitimoTheme.spacing.xs)
        ) {
            if (completionsCount > 0) {
                Text(
                    text = "Consistência em construção",
                    style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = RitimoTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))
                Text(
                    text = "Você possui $completionsCount treino(s) concluído(s) registrados. O mapa de evolução muscular será liberado com mais registros.",
                    style = RitimoTheme.typography.bodyMedium,
                    color = RitimoTheme.colors.onSurfaceVariant
                )
            } else {
                Text(
                    text = "Ainda estamos construindo sua evolução",
                    style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = RitimoTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))
                Text(
                    text = "Conclua seus treinos prescritos para visualizar seu progresso e mapa de estímulo muscular.",
                    style = RitimoTheme.typography.bodyMedium,
                    color = RitimoTheme.colors.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun RhythmSection(completionsCount: Int) {
    Text(
        text = "SEU RITMO",
        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = RitimoTheme.colors.onBackground
    )

    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

    RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RitimoTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🔥",
                style = RitimoTheme.typography.displayMedium
            )

            Spacer(modifier = Modifier.width(RitimoTheme.spacing.md))

            Column {
                Text(
                    text = if (completionsCount > 0) "$completionsCount treino(s) realizados" else "Seu ritmo começa hoje",
                    style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = RitimoTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))
                Text(
                    text = "A constância gera resultados estéticos e de saúde.",
                    style = RitimoTheme.typography.bodyMedium,
                    color = RitimoTheme.colors.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ProfessorMessageSection() {
    Text(
        text = "DO SEU PROFESSOR",
        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = RitimoTheme.colors.onBackground
    )

    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

    RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RitimoTheme.spacing.xs)
        ) {
            Text(
                text = "Nenhuma mensagem do seu professor hoje",
                style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = RitimoTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))
            Text(
                text = "Orientações e avisos do seu treinador aparecerão nesta seção.",
                style = RitimoTheme.typography.bodyMedium,
                color = RitimoTheme.colors.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DailySummarySection(data: StudentTodayData) {
    val totalWorkouts = data.assignedWorkouts.size
    val completedWorkouts = data.completions.size.coerceAtMost(totalWorkouts)

    val activeDiet = data.activeDiet
    val totalMeals = activeDiet?.meals?.size ?: 0
    val completedMeals = if (activeDiet != null) {
        data.mealCompletions.count { it.dietId == activeDiet.id }
    } else 0

    val waterGoalReached = data.waterTracking.consumedMl >= data.waterTracking.dailyGoalMl

    var totalGoals = 0
    var completedGoals = 0

    if (totalWorkouts > 0) {
        totalGoals += totalWorkouts
        completedGoals += completedWorkouts
    }

    if (totalMeals > 0) {
        totalGoals += totalMeals
        completedGoals += completedMeals
    }

    totalGoals += 1
    if (waterGoalReached) {
        completedGoals += 1
    }

    val progressPercent = if (totalGoals > 0) (completedGoals * 100 / totalGoals) else 0

    RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RitimoTheme.spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RitimoStatusBadge(text = "RESUMO DO DIA", type = BadgeType.PRIMARY)

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

            Text(
                text = "$progressPercent% do dia concluído",
                style = RitimoTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                color = RitimoTheme.colors.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

            Text(
                text = "Continue no ritmo 🔥",
                style = RitimoTheme.typography.titleMedium,
                color = RitimoTheme.colors.primary,
                textAlign = TextAlign.Center
            )
        }
    }
}
