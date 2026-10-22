package com.ranielschneider.ritimofit.presentation.student

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ranielschneider.ritimofit.core.design.components.button.RitimoPrimaryButton
import com.ranielschneider.ritimofit.core.design.components.button.RitimoSecondaryButton
import com.ranielschneider.ritimofit.core.design.components.button.RitimoTertiaryButton
import com.ranielschneider.ritimofit.core.design.components.card.RitimoStandardCard
import com.ranielschneider.ritimofit.core.design.components.indicator.RitimoWaterBottle
import com.ranielschneider.ritimofit.core.design.components.state.RitimoEmptyState
import com.ranielschneider.ritimofit.core.design.components.state.RitimoErrorState
import com.ranielschneider.ritimofit.core.design.components.state.RitimoLoadingState
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme
import com.ranielschneider.ritimofit.domain.model.Diet
import com.ranielschneider.ritimofit.domain.model.Meal
import com.ranielschneider.ritimofit.domain.model.User
import com.ranielschneider.ritimofit.domain.model.WaterTracking
import com.ranielschneider.ritimofit.domain.model.Workout
import com.ranielschneider.ritimofit.presentation.base.UiState
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentTodayData
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentTodayViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.jetbrains.compose.resources.painterResource
import ritimofit.shared.generated.resources.Res
import ritimofit.shared.generated.resources.workout_hero

@Composable
fun StudentTodayScreen(
    onViewWorkout: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StudentTodayViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize().background(RitimoTheme.colors.background)) {
        when (val state = uiState) {
            is UiState.Loading -> RitimoLoadingState(
                message = "Carregando sua rotina...",
                modifier = Modifier.fillMaxSize()
            )
            is UiState.Error -> RitimoErrorState(
                title = "Erro ao carregar",
                message = "Não foi possível carregar sua rotina de hoje.",
                onRetry = viewModel::loadTodayContent,
                modifier = Modifier.fillMaxSize()
            )
            is UiState.Success -> {
                val data = state.data
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = RitimoTheme.spacing.lg)
                ) {
                    Spacer(Modifier.height(RitimoTheme.spacing.md))
                    StudentTodayHeader(data.studentUser)
                    Spacer(Modifier.height(RitimoTheme.spacing.lg))
                    DailyOverview(data)
                    Spacer(Modifier.height(RitimoTheme.spacing.md))
                    PriorityWorkoutSection(
                        data = data,
                        onViewWorkout = onViewWorkout,
                        onCompleteWorkout = viewModel::completeWorkout
                    )
                    Spacer(Modifier.height(RitimoTheme.spacing.md))
                    HydrationSection(
                        water = data.waterTracking,
                        onAddWater = viewModel::addWater,
                        onRemoveWater = viewModel::removeWater
                    )
                    Spacer(Modifier.height(RitimoTheme.spacing.md))
                    NutritionSection(
                        data = data,
                        onCompleteMeal = viewModel::completeMeal,
                        onUndoMealCompletion = viewModel::undoMealCompletion
                    )
                    Spacer(Modifier.height(RitimoTheme.spacing.xxl))
                }
            }
            else -> Unit
        }
    }
}

@Composable
private fun StudentTodayHeader(studentUser: User) {
    val firstName = studentUser.fullName.trim().split(Regex("\\s+")).firstOrNull().orEmpty()

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "OLÁ, ${firstName.uppercase()}",
                style = RitimoTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                color = RitimoTheme.colors.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(RitimoTheme.spacing.xxs))
            Text(
                text = "O teu dia",
                style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                color = RitimoTheme.colors.onBackground
            )
            Text(
                text = "Vamos manter o ritmo hoje.",
                style = RitimoTheme.typography.bodyMedium,
                color = RitimoTheme.colors.onSurfaceVariant
            )
        }

        Spacer(Modifier.width(RitimoTheme.spacing.md))
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(RitimoTheme.colors.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = firstName.firstOrNull()?.uppercase() ?: "•",
                style = RitimoTheme.typography.headlineMedium,
                color = RitimoTheme.colors.primary
            )
        }
    }
}

@Composable
private fun DailyOverview(data: StudentTodayData) {
    val diet = data.activeDiet
    val water = data.waterTracking
    val assignedWorkoutIds = data.assignedWorkouts.mapTo(mutableSetOf()) { it.id }
    val completedWorkoutIds = data.completions
        .asSequence()
        .map { it.workoutId }
        .filter { it in assignedWorkoutIds }
        .toSet()
    val mealIds = diet?.meals.orEmpty().mapTo(mutableSetOf()) { it.id }
    val completedMealIds = data.mealCompletions
        .asSequence()
        .filter { it.dietId == diet?.id && it.mealId in mealIds }
        .map { it.mealId }
        .toSet()
    val consumedWaterMl = water.consumedMl.coerceAtLeast(0)
    val dailyWaterGoalMl = water.dailyGoalMl.coerceAtLeast(0)
    val workoutProgress = RingProgress(completedWorkoutIds.size, assignedWorkoutIds.size)
    val hydrationProgress = RingProgress(consumedWaterMl, dailyWaterGoalMl)
    val mealProgress = RingProgress(completedMealIds.size, mealIds.size)

    RitimoStandardCard(
        modifier = Modifier.fillMaxWidth(),
        padding = RitimoTheme.spacing.md,
        containerColor = RitimoTheme.colors.surface,
        borderColor = RitimoTheme.colors.outlineVariant
    ) {
        Column {
            Text(
                text = "RESUMO DE HOJE",
                style = RitimoTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = RitimoTheme.colors.onSurfaceVariant
            )
            Spacer(Modifier.height(RitimoTheme.spacing.sm))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.sm)
                ) {
                    OverviewMetric(
                        value = "${workoutProgress.completed} / ${workoutProgress.total}",
                        label = "treinos concluídos",
                        icon = RitimoIcons.Workouts,
                        tint = RitimoTheme.colors.primary
                    )
                    OverviewMetric(
                        value = "${formatLiters(consumedWaterMl)} / ${formatLiters(dailyWaterGoalMl)} L",
                        label = "água consumida",
                        icon = null,
                        tint = RitimoTheme.colors.secondary
                    )
                    OverviewMetric(
                        value = "${mealProgress.completed} / ${mealProgress.total}",
                        label = "refeições concluídas",
                        icon = RitimoIcons.Diet,
                        tint = MealPink
                    )
                }
                Spacer(Modifier.width(RitimoTheme.spacing.xs))
                BoxWithConstraints {
                    val ringSize = (maxWidth * 0.46f).coerceIn(116.dp, 148.dp)
                    DailyProgressRings(
                        workoutProgress = workoutProgress,
                        hydrationProgress = hydrationProgress,
                        mealProgress = mealProgress,
                        modifier = Modifier.size(ringSize)
                    )
                }
            }
        }
    }
}

@Composable
private fun OverviewMetric(
    value: String,
    label: String,
    icon: ImageVector?,
    tint: androidx.compose.ui.graphics.Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = tint
            )
        } else WaterDropMark()
        Spacer(Modifier.width(RitimoTheme.spacing.xs))
        Column {
            Text(
                text = value,
                style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = RitimoTheme.colors.onSurface,
                maxLines = 1
            )
            Text(
                text = label,
                style = RitimoTheme.typography.caption,
                color = RitimoTheme.colors.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private data class RingProgress(val completed: Int, val total: Int) {
    val fraction: Float
        get() = if (total > 0) (completed.toFloat() / total).coerceIn(0f, 1f) else 0f
}

@Composable
private fun DailyProgressRings(
    workoutProgress: RingProgress,
    hydrationProgress: RingProgress,
    mealProgress: RingProgress,
    modifier: Modifier = Modifier
) {
    val rings = listOf(
        workoutProgress to RitimoTheme.colors.primary,
        hydrationProgress to WaterCyan,
        mealProgress to MealPink
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 8.dp.toPx()
            val gap = 5.dp.toPx()
            val outerDiameter = size.minDimension - strokeWidth
            rings.forEachIndexed { index, (progress, color) ->
                val diameter = outerDiameter - index * (strokeWidth * 2f + gap)
                val topLeft = androidx.compose.ui.geometry.Offset(
                    (size.width - diameter) / 2f,
                    (size.height - diameter) / 2f
                )
                val arcSize = androidx.compose.ui.geometry.Size(diameter, diameter)
                drawArc(
                    color = Color(0xFF252A31),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                if (progress.total > 0 && progress.fraction > 0f) {
                    drawArc(
                        color = color,
                        startAngle = -90f,
                        sweepAngle = 360f * progress.fraction,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "HOJE",
                style = RitimoTheme.typography.caption,
                color = RitimoTheme.colors.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun PriorityWorkoutSection(
    data: StudentTodayData,
    onViewWorkout: (String) -> Unit,
    onCompleteWorkout: (String) -> Unit
) {
    SectionHeading(title = "TREINO EM DESTAQUE", supportingText = "Prioridade do teu plano")
    Spacer(Modifier.height(RitimoTheme.spacing.xs))

    val workout = data.assignedWorkouts.firstOrNull()
    if (workout == null) {
        RitimoEmptyState(
            title = "Sem treinos atribuídos",
            description = "Os treinos do teu plano aparecerão aqui.",
            icon = RitimoIcons.Workouts,
            modifier = Modifier.fillMaxWidth()
        )
        return
    }

    val hasCompletionRecord = data.completions.any { it.workoutId == workout.id }
    RitimoStandardCard(
        modifier = Modifier.fillMaxWidth(),
        padding = 0.dp,
        containerColor = Color(0xFF101214),
        borderColor = RitimoTheme.colors.outlineVariant
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 340.dp)
        ) {
            Image(
                painter = painterResource(Res.drawable.workout_hero),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(
                            0f to Color(0xFF0C0E10).copy(alpha = 0.98f),
                            0.42f to Color(0xFF0C0E10).copy(alpha = 0.90f),
                            0.72f to Color(0xFF0C0E10).copy(alpha = 0.48f),
                            1f to Color(0xFF0C0E10).copy(alpha = 0.10f)
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.78f)
                    .heightIn(min = 340.dp)
                    .padding(RitimoTheme.spacing.md),
                verticalArrangement = Arrangement.Center
            ) {
                Column {
                    Text(
                        text = workout.title,
                        style = RitimoTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Spacer(Modifier.height(RitimoTheme.spacing.xs))
                    Text(
                        text = workout.description?.takeIf(String::isNotBlank)
                            ?: "${workout.exercises.size} exercícios no plano",
                        style = RitimoTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.78f),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(RitimoTheme.spacing.xs))
                Text(
                    text = "${workout.exercises.size} exercícios",
                    style = RitimoTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White.copy(alpha = 0.76f)
                )
                Spacer(Modifier.height(RitimoTheme.spacing.md))
                RitimoPrimaryButton(
                    text = "Ver treino",
                    onClick = { onViewWorkout(workout.id) },
                    modifier = Modifier.fillMaxWidth()
                )
                if (!hasCompletionRecord) {
                    Spacer(Modifier.height(RitimoTheme.spacing.xs))
                    RitimoSecondaryButton(
                        text = "Marcar concluído",
                        onClick = { onCompleteWorkout(workout.id) },
                        modifier = Modifier.fillMaxWidth(),
                        contentColor = Color.White
                    )
                } else {
                    RitimoSecondaryButton(
                        text = "Treino concluído",
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = RitimoTheme.spacing.xs),
                        enabled = false,
                        icon = RitimoIcons.Check,
                        contentColor = Color.White
                    )
                }
            }
        }
    }

    val remainingWorkouts = data.assignedWorkouts.size - 1
    if (remainingWorkouts > 0) {
        Spacer(Modifier.height(RitimoTheme.spacing.xs))
        Text(
            text = "+ $remainingWorkouts ${if (remainingWorkouts == 1) "outro treino" else "outros treinos"} no separador Treinos",
            style = RitimoTheme.typography.bodySmall,
            color = RitimoTheme.colors.onSurfaceVariant
        )
    }
}

@Composable
private fun HydrationSection(
    water: WaterTracking,
    onAddWater: (Int) -> Unit,
    onRemoveWater: (Int) -> Unit
) {
    val goal = water.dailyGoalMl.coerceAtLeast(0)
    val recorded = water.consumedMl.coerceAtLeast(0)
    RitimoStandardCard(
        modifier = Modifier.fillMaxWidth(),
        padding = RitimoTheme.spacing.sm,
        containerColor = RitimoTheme.colors.surface,
        borderColor = RitimoTheme.colors.outlineVariant
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    WaterDropMark()
                    Spacer(Modifier.width(RitimoTheme.spacing.xs))
                    Column {
                        Text(
                            text = "Hidratação",
                            style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = RitimoTheme.colors.onSurface
                        )
                        Text(
                            text = "Pequenos goles, grande diferença.",
                            style = RitimoTheme.typography.bodySmall,
                            color = RitimoTheme.colors.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Spacer(Modifier.height(RitimoTheme.spacing.sm))
                Text(
                    text = "${formatLiters(recorded)} / ${formatLiters(goal)} L",
                    style = RitimoTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = RitimoTheme.colors.onSurface,
                    maxLines = 1
                )
                Spacer(Modifier.height(RitimoTheme.spacing.xs))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .clip(RitimoTheme.shapes.pill)
                        .background(RitimoTheme.colors.surfaceVariant)
                ) {
                    if (goal > 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth((recorded.toFloat() / goal).coerceIn(0f, 1f))
                                .height(7.dp)
                                .clip(RitimoTheme.shapes.pill)
                                .background(RitimoTheme.colors.secondary)
                        )
                    }
                }
            }
            Spacer(Modifier.width(RitimoTheme.spacing.xs))
            RitimoWaterBottle(
                consumedMl = recorded,
                dailyGoalMl = goal,
                size = 64.dp,
                height = 120.dp
            )
            Spacer(Modifier.width(RitimoTheme.spacing.xs))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(RitimoTheme.colors.primary)
                        .clickable(onClick = { onAddWater(250) }),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = RitimoIcons.Add,
                        contentDescription = "Adicionar 250 ml de água",
                        modifier = Modifier.size(24.dp),
                        tint = RitimoTheme.colors.onPrimary
                    )
                }
                if (recorded > 0) {
                    val removeTint = RitimoTheme.colors.onSurfaceVariant
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(RitimoTheme.colors.surfaceVariant)
                            .clickable(onClick = { onRemoveWater(250) })
                            .semantics {
                                contentDescription = "Remover 250 ml de água"
                                role = Role.Button
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(Modifier.size(16.dp)) {
                            drawLine(
                                color = removeTint,
                                start = androidx.compose.ui.geometry.Offset(0f, size.height / 2f),
                                end = androidx.compose.ui.geometry.Offset(size.width, size.height / 2f),
                                strokeWidth = 2.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        }
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
    var showAllMeals by remember(activeDiet?.id) { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = RitimoIcons.Diet,
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = MealPink
        )
        Spacer(Modifier.width(RitimoTheme.spacing.xs))
        SectionHeading(title = "ALIMENTAÇÃO", supportingText = activeDiet?.title ?: "Plano alimentar")
    }
    Spacer(Modifier.height(RitimoTheme.spacing.xs))

    if (activeDiet == null) {
        RitimoEmptyState(
            title = "Plano alimentar indisponível",
            description = "Quando existir uma dieta ativa, o resumo será apresentado aqui.",
            icon = RitimoIcons.Diet,
            modifier = Modifier.fillMaxWidth()
        )
        return
    }

    val meals = activeDiet.meals.sortedBy(Meal::order)
    val nextMeal = meals.firstOrNull { meal ->
        data.mealCompletions.none { it.dietId == activeDiet.id && it.mealId == meal.id }
    } ?: meals.firstOrNull()

    RitimoStandardCard(
        modifier = Modifier.fillMaxWidth(),
        padding = RitimoTheme.spacing.md,
        containerColor = RitimoTheme.colors.surface,
        borderColor = RitimoTheme.colors.outlineVariant
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${activeDiet.meals.size} refeições no plano",
                        style = RitimoTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = RitimoTheme.colors.onSurfaceVariant
                    )
                    Text(
                        text = if (nextMeal == null) "Sem refeições registadas" else "Próxima no plano",
                        style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = RitimoTheme.colors.onSurface
                    )
                }
                Icon(
                    imageVector = RitimoIcons.Diet,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = RitimoTheme.colors.primary
                )
            }

            if (nextMeal != null) {
                Spacer(Modifier.height(RitimoTheme.spacing.sm))
                MealSummary(
                    diet = activeDiet,
                    meal = nextMeal,
                    data = data,
                    onCompleteMeal = onCompleteMeal,
                    onUndoMealCompletion = onUndoMealCompletion
                )
            }

            if (meals.size > 1) {
                Spacer(Modifier.height(RitimoTheme.spacing.xs))
                RitimoTertiaryButton(
                    text = if (showAllMeals) "Ocultar restantes" else "Ver todas as refeições",
                    onClick = { showAllMeals = !showAllMeals },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (showAllMeals) {
                meals.filterNot { it.id == nextMeal?.id }.forEach { meal ->
                    Spacer(Modifier.height(RitimoTheme.spacing.sm))
                    MealSummary(
                        diet = activeDiet,
                        meal = meal,
                        data = data,
                        onCompleteMeal = onCompleteMeal,
                        onUndoMealCompletion = onUndoMealCompletion
                    )
                }
            }
        }
    }
}

@Composable
private fun MealSummary(
    diet: Diet,
    meal: Meal,
    data: StudentTodayData,
    onCompleteMeal: (dietId: String, mealId: String) -> Unit,
    onUndoMealCompletion: (dietId: String, mealId: String) -> Unit
) {
    val isCompleted = data.mealCompletions.any {
        it.dietId == diet.id && it.mealId == meal.id
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RitimoTheme.shapes.small)
            .background(RitimoTheme.colors.surfaceVariant.copy(alpha = 0.55f))
            .padding(RitimoTheme.spacing.sm)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = meal.name,
                style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = RitimoTheme.colors.onSurface,
                modifier = Modifier.weight(1f)
            )
            meal.time?.takeIf(String::isNotBlank)?.let { time ->
                Spacer(Modifier.width(RitimoTheme.spacing.xs))
                Text(
                    text = time,
                    style = RitimoTheme.typography.labelMedium,
                    color = RitimoTheme.colors.onSurfaceVariant
                )
            }
        }

        val foodsToShow = if (meal.foods.size <= 2 || data.activeDiet?.meals?.size == 1) {
            meal.foods
        } else {
            meal.foods.take(2)
        }
        if (foodsToShow.isNotEmpty()) {
            Spacer(Modifier.height(RitimoTheme.spacing.xxs))
            Text(
                text = foodsToShow.joinToString(" · ") { food -> "${food.name} ${food.amount} ${food.unit}" },
                style = RitimoTheme.typography.bodySmall,
                color = RitimoTheme.colors.onSurfaceVariant
            )
        }
        if (!meal.notes.isNullOrBlank()) {
            Spacer(Modifier.height(RitimoTheme.spacing.xxs))
            Text(
                text = meal.notes,
                style = RitimoTheme.typography.bodySmall,
                color = RitimoTheme.colors.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(RitimoTheme.spacing.xs))
        if (isCompleted) {
            RitimoSecondaryButton(
                text = "Desfazer conclusão",
                onClick = { onUndoMealCompletion(diet.id, meal.id) },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            RitimoPrimaryButton(
                text = "Marcar refeição concluída",
                onClick = { onCompleteMeal(diet.id, meal.id) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SectionHeading(title: String, supportingText: String) {
    Column {
        Text(
            text = title,
            style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = RitimoTheme.colors.onBackground
        )
        Text(
            text = supportingText,
            style = RitimoTheme.typography.bodySmall,
            color = RitimoTheme.colors.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun WaterDropMark() {
    val color = RitimoTheme.colors.secondary
    Canvas(modifier = Modifier.size(22.dp)) {
        val drop = Path().apply {
            moveTo(size.width / 2f, 0f)
            cubicTo(size.width * 0.38f, size.height * 0.22f, size.width * 0.05f, size.height * 0.55f, size.width * 0.05f, size.height * 0.70f)
            cubicTo(size.width * 0.05f, size.height * 0.88f, size.width * 0.25f, size.height, size.width / 2f, size.height)
            cubicTo(size.width * 0.75f, size.height, size.width * 0.95f, size.height * 0.88f, size.width * 0.95f, size.height * 0.70f)
            cubicTo(size.width * 0.95f, size.height * 0.55f, size.width * 0.62f, size.height * 0.22f, size.width / 2f, 0f)
            close()
        }
        drawPath(path = drop, color = color)
    }
}

private val MealPink = Color(0xFFE75491)
private val WaterCyan = Color(0xFF20C9E8)

private fun formatLiters(ml: Int): String {
    val tenths = ml.coerceAtLeast(0) / 100
    return "${tenths / 10},${tenths % 10}"
}
