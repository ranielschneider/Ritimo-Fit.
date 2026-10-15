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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.clip
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
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.core.design.theme.RitimoTheme
import com.ranielschneider.ritimofit.domain.calculator.NutritionalCalculator
import com.ranielschneider.ritimofit.domain.model.Food
import com.ranielschneider.ritimofit.domain.model.Meal
import com.ranielschneider.ritimofit.presentation.base.UiState
import com.ranielschneider.ritimofit.presentation.viewmodel.DietViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DietEditorScreen(
    dietIdToEdit: String?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DietViewModel = koinViewModel()
) {
    LaunchedEffect(dietIdToEdit) {
        if (dietIdToEdit != null && viewModel.currentDietId.value != dietIdToEdit) {
            viewModel.loadDietForEditing(dietIdToEdit)
        } else if (viewModel.currentDietId.value == null && viewModel.dietTitle.value.isEmpty()) {
            viewModel.initNewDiet()
        }
    }

    val dietTitle by viewModel.dietTitle.collectAsState()
    val dietDescription by viewModel.dietDescription.collectAsState()
    val totalCalories by viewModel.totalCalories.collectAsState()
    val proteinGrams by viewModel.proteinGrams.collectAsState()
    val carbsGrams by viewModel.carbsGrams.collectAsState()
    val fatsGrams by viewModel.fatsGrams.collectAsState()
    val meals by viewModel.meals.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val saveError by viewModel.saveError.collectAsState()
    val currentId by viewModel.currentDietId.collectAsState()

    val calculatedSummary = viewModel.getCalculatedDietSummary()

    var showAddMealDialog by remember { mutableStateOf(false) }
    var activeMealIndexForFoodDialog by remember { mutableStateOf<Int?>(null) }

    val scrollState = rememberScrollState()
    val isEditMode = currentId != null

    RitimoAmbientBackground(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = RitimoTheme.spacing.lg)
                .navigationBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

            // 1. Header Section
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RitimoIconButton(
                    icon = RitimoIcons.ArrowBack,
                    contentDescription = "Voltar para biblioteca de dietas",
                    onClick = onBack
                )

                Spacer(modifier = Modifier.width(RitimoTheme.spacing.xs))

                RitimoStatusBadge(
                    text = if (isEditMode) "EDITAR DIETA" else "NOVA DIETA",
                    type = BadgeType.PRIMARY
                )
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

            Text(
                text = if (isEditMode) "Editar Plano Alimentar" else "Criar Plano Alimentar",
                style = RitimoTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                color = RitimoTheme.colors.onBackground
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xxs))

            Text(
                text = "Defina nome, metas de macros e monte as refeições com alimentos",
                style = RitimoTheme.typography.bodyMedium,
                color = RitimoTheme.colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.lg))

            // 2. Info & Macros Section
            RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
                    RitimoTextField(
                        value = dietTitle,
                        onValueChange = viewModel::onTitleChanged,
                        label = "Nome do plano alimentar",
                        placeholder = "Ex.: Hipertrofia Moderada 2600 kcal",
                        errorMessage = saveError,
                        modifier = Modifier.fillMaxWidth()
                    )

                    RitimoTextField(
                        value = dietDescription,
                        onValueChange = viewModel::onDescriptionChanged,
                        label = "Descrição (Opcional)",
                        placeholder = "Ex.: Foco em proteína alta e ingestão constante de água",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

                    Text(
                        text = "Metas Nutricionais Diárias (Opcionais)",
                        style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = RitimoTheme.colors.onSurface
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.sm)
                    ) {
                        RitimoTextField(
                            value = totalCalories,
                            onValueChange = viewModel::onTotalCaloriesChanged,
                            label = "Calorias (kcal)",
                            placeholder = "2600",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )

                        RitimoTextField(
                            value = proteinGrams,
                            onValueChange = viewModel::onProteinGramsChanged,
                            label = "Proteínas (g)",
                            placeholder = "180",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.sm)
                    ) {
                        RitimoTextField(
                            value = carbsGrams,
                            onValueChange = viewModel::onCarbsGramsChanged,
                            label = "Carboidratos (g)",
                            placeholder = "320",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )

                        RitimoTextField(
                            value = fatsGrams,
                            onValueChange = viewModel::onFatsGramsChanged,
                            label = "Gorduras (g)",
                            placeholder = "70",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

                    // Calculated Total Summary Badge Row
                    Text(
                        text = "Total Calculado das Refeições:",
                        style = RitimoTheme.typography.labelSmall,
                        color = RitimoTheme.colors.onSurfaceVariant
                    )
                    RitimoStatusBadge(
                        text = "${calculatedSummary.totalCaloriesKcal} KCAL • ${calculatedSummary.proteinGrams}g P • ${calculatedSummary.carbsGrams}g C • ${calculatedSummary.fatGrams}g G",
                        type = BadgeType.PRIMARY
                    )
                }
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

            // 3. Meals Header & Add Meal Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Refeições (${meals.size})",
                    style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = RitimoTheme.colors.onBackground
                )

                RitimoSecondaryButton(
                    text = "+ Refeição",
                    onClick = { showAddMealDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.md))

            // 4. Meals & Foods List
            if (meals.isEmpty()) {
                RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(RitimoTheme.spacing.md),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Nenhuma refeição cadastrada",
                            style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = RitimoTheme.colors.onSurface
                        )
                        Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
                        Text(
                            text = "Toque em '+ Refeição' para adicionar refeições e alimentos ao plano.",
                            style = RitimoTheme.typography.bodyMedium,
                            color = RitimoTheme.colors.onSurfaceVariant
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
                    meals.forEachIndexed { mealIndex, meal ->
                        MealCardItem(
                            meal = meal,
                            onAddFood = { activeMealIndexForFoodDialog = mealIndex },
                            onRemoveMeal = { viewModel.removeMeal(mealIndex) },
                            onRemoveFood = { foodIndex -> viewModel.removeFoodFromMeal(mealIndex, foodIndex) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(RitimoTheme.spacing.xl))

            // 5. Save Button
            RitimoPrimaryButton(
                text = if (isSaving) "Salvando..." else "Salvar plano alimentar",
                onClick = { viewModel.saveDiet(onSuccess = onBack) },
                enabled = !isSaving,
                icon = RitimoIcons.Check,
                modifier = Modifier.fillMaxWidth()
            )

            // Extra bottom spacing so the save button and last meal scroll completely clear of the floating bottom navigation bar
            Spacer(modifier = Modifier.height(120.dp))
        }

        // Add Meal Dialog
        if (showAddMealDialog) {
            AddMealDialog(
                onDismiss = { showAddMealDialog = false },
                onConfirm = { name, time ->
                    viewModel.addMeal(name, time)
                    showAddMealDialog = false
                }
            )
        }

        // Add Food Dialog
        activeMealIndexForFoodDialog?.let { mealIndex ->
            val meal = meals.getOrNull(mealIndex)
            if (meal != null) {
                AddFoodCatalogDialog(
                    mealName = meal.name,
                    viewModel = viewModel,
                    onDismiss = { activeMealIndexForFoodDialog = null },
                    onConfirmCatalogPortion = { food, amount, unit, notes ->
                        viewModel.addFoodPortionToMeal(
                            mealIndex = mealIndex,
                            food = food,
                            amountValue = amount,
                            unit = unit,
                            notes = notes
                        )
                        activeMealIndexForFoodDialog = null
                    },
                    onConfirmCustomFood = { name, amount, unit, kcal, p, c, f, notes ->
                        viewModel.addCustomFoodToMeal(
                            mealIndex = mealIndex,
                            foodName = name,
                            amount = amount,
                            unit = unit,
                            caloriesKcal = kcal,
                            proteinGrams = p,
                            carbsGrams = c,
                            fatGrams = f,
                            notes = notes
                        )
                        activeMealIndexForFoodDialog = null
                    }
                )
            }
        }
    }
}

@Composable
private fun MealCardItem(
    meal: Meal,
    onAddFood: () -> Unit,
    onRemoveMeal: () -> Unit,
    onRemoveFood: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val mealSummary = NutritionalCalculator.sumMealMacros(meal)

    RitimoGlassCard(modifier = modifier) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = meal.name,
                        style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = RitimoTheme.colors.onSurface
                    )

                    if (!meal.time.isNullOrBlank()) {
                        Spacer(modifier = Modifier.width(RitimoTheme.spacing.xs))
                        RitimoStatusBadge(
                            text = meal.time,
                            type = BadgeType.NEUTRAL
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RitimoSecondaryButton(
                        text = "+ Alimento",
                        onClick = onAddFood
                    )

                    RitimoIconButton(
                        icon = RitimoIcons.Trash,
                        contentDescription = "Excluir refeição ${meal.name}",
                        onClick = onRemoveMeal
                    )
                }
            }

            if (meal.foods.isNotEmpty()) {
                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))
                RitimoStatusBadge(
                    text = "${mealSummary.caloriesKcal} kcal • ${mealSummary.proteinGrams}g P • ${mealSummary.carbsGrams}g C • ${mealSummary.fatGrams}g G",
                    type = BadgeType.PRIMARY
                )
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
                    meal.foods.forEachIndexed { foodIndex, food ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = RitimoTheme.spacing.xxs),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = food.name,
                                        style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = RitimoTheme.colors.onSurface
                                    )
                                    if (food.isCustom) {
                                        Spacer(modifier = Modifier.width(RitimoTheme.spacing.xs))
                                        RitimoStatusBadge(text = "PERSONALIZADO", type = BadgeType.NEUTRAL)
                                    }
                                }
                                Text(
                                    text = "${food.amount} ${food.unit} • ${food.caloriesKcal} kcal (${food.proteinGrams}g P, ${food.carbsGrams}g C, ${food.fatGrams}g G)" + (food.notes?.let { " • $it" } ?: ""),
                                    style = RitimoTheme.typography.bodyMedium,
                                    color = RitimoTheme.colors.primary
                                )
                            }

                            RitimoIconButton(
                                icon = RitimoIcons.Clear,
                                contentDescription = "Remover alimento ${food.name}",
                                onClick = { onRemoveFood(foodIndex) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddMealDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, time: String?) -> Unit
) {
    var mealName by remember { mutableStateOf("") }
    var mealTime by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Nova Refeição",
                style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
                RitimoTextField(
                    value = mealName,
                    onValueChange = { mealName = it },
                    label = "Nome da refeição",
                    placeholder = "Ex.: Lanche da manhã"
                )

                RitimoTextField(
                    value = mealTime,
                    onValueChange = { mealTime = it },
                    label = "Horário (Opcional)",
                    placeholder = "Ex.: 10:00"
                )
            }
        },
        confirmButton = {
            RitimoPrimaryButton(
                text = "Adicionar",
                onClick = {
                    if (mealName.isNotBlank()) {
                        onConfirm(mealName.trim(), mealTime.trim().takeIf { it.isNotEmpty() })
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
private fun AddFoodCatalogDialog(
    mealName: String,
    viewModel: DietViewModel,
    onDismiss: () -> Unit,
    onConfirmCatalogPortion: (food: Food, amount: Double, unit: String, notes: String?) -> Unit,
    onConfirmCustomFood: (name: String, amount: String, unit: String, kcal: Double, p: Double, c: Double, f: Double, notes: String?) -> Unit
) {
    var activeTab by remember { mutableStateOf(0) } // 0 = Catalog Search, 1 = Custom Food

    val searchQuery by viewModel.foodSearchQuery.collectAsState()
    val searchResultsState by viewModel.foodSearchResults.collectAsState()

    var selectedFood by remember { mutableStateOf<Food?>(null) }
    var portionAmountStr by remember { mutableStateOf("100") }
    var portionUnit by remember { mutableStateOf("g") }
    var portionNotes by remember { mutableStateOf("") }

    // Custom Food Form Fields
    var customName by remember { mutableStateOf("") }
    var customAmount by remember { mutableStateOf("100") }
    var customUnit by remember { mutableStateOf("g") }
    var customKcal by remember { mutableStateOf("") }
    var customProtein by remember { mutableStateOf("") }
    var customCarbs by remember { mutableStateOf("") }
    var customFat by remember { mutableStateOf("") }
    var customNotes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "Adicionar em $mealName",
                    style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

                // Sub-tabs: Catálogo vs Personalizado
                Row(horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.xs)) {
                    Box(
                        modifier = Modifier
                            .clip(RitimoTheme.shapes.pill)
                            .background(if (activeTab == 0) RitimoTheme.colors.primaryContainer else RitimoTheme.colors.surface)
                            .border(1.dp, if (activeTab == 0) RitimoTheme.colors.primary else RitimoTheme.colors.outlineVariant, RitimoTheme.shapes.pill)
                            .clickable { activeTab = 0 }
                            .padding(horizontal = RitimoTheme.spacing.md, vertical = RitimoTheme.spacing.xs)
                    ) {
                        Text(
                            text = "Catálogo TACO",
                            style = RitimoTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (activeTab == 0) RitimoTheme.colors.primary else RitimoTheme.colors.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RitimoTheme.shapes.pill)
                            .background(if (activeTab == 1) RitimoTheme.colors.primaryContainer else RitimoTheme.colors.surface)
                            .border(1.dp, if (activeTab == 1) RitimoTheme.colors.primary else RitimoTheme.colors.outlineVariant, RitimoTheme.shapes.pill)
                            .clickable { activeTab = 1 }
                            .padding(horizontal = RitimoTheme.spacing.md, vertical = RitimoTheme.spacing.xs)
                    ) {
                        Text(
                            text = "Personalizado",
                            style = RitimoTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (activeTab == 1) RitimoTheme.colors.primary else RitimoTheme.colors.onSurfaceVariant
                        )
                    }
                }
            }
        },
        text = {
            if (activeTab == 0) {
                // Tab 0: Food Catalog Search
                Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
                    RitimoTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.searchCatalogFoods(it) },
                        label = "Buscar alimento",
                        placeholder = "Ex.: frango, arroz, ovo, banana..."
                    )

                    if (selectedFood == null) {
                        Text(
                            text = "Resultados da busca:",
                            style = RitimoTheme.typography.labelSmall,
                            color = RitimoTheme.colors.onSurfaceVariant
                        )

                        when (val state = searchResultsState) {
                            is UiState.Loading -> {
                                Text(
                                    text = "Buscando no catálogo...",
                                    style = RitimoTheme.typography.bodyMedium,
                                    color = RitimoTheme.colors.onSurfaceVariant
                                )
                            }
                            is UiState.Error -> {
                                Text(
                                    text = "Erro ao carregar catálogo.",
                                    style = RitimoTheme.typography.bodyMedium,
                                    color = RitimoTheme.colors.error
                                )
                            }
                            is UiState.Success -> {
                                val foods = state.data
                                if (foods.isEmpty()) {
                                    Text(
                                        text = "Nenhum alimento encontrado no catálogo.",
                                        style = RitimoTheme.typography.bodyMedium,
                                        color = RitimoTheme.colors.onSurfaceVariant
                                    )
                                } else {
                                    LazyColumn(
                                        modifier = Modifier.height(180.dp)
                                    ) {
                                        items(foods) { food ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable { selectedFood = food }
                                                    .padding(vertical = RitimoTheme.spacing.xs),
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
                                                        text = "${food.caloriesPer100g} kcal / 100g (${food.proteinPer100g}g P, ${food.carbsPer100g}g C, ${food.fatPer100g}g G)",
                                                        style = RitimoTheme.typography.bodySmall,
                                                        color = RitimoTheme.colors.onSurfaceVariant
                                                    )
                                                }
                                                RitimoStatusBadge(text = "Selecionar", type = BadgeType.PRIMARY)
                                            }
                                        }
                                    }
                                }
                            }
                            else -> {}
                        }
                    } else {
                        // Food Selected -> Portion Amount & Unit Setup
                        val food = selectedFood!!
                        val amountVal = portionAmountStr.toDoubleOrNull() ?: 100.0
                        val calculatedPortion = NutritionalCalculator.calculatePortion(
                            food = food,
                            amountValue = amountVal,
                            unit = portionUnit
                        )

                        RitimoGlassCard(modifier = Modifier.fillMaxWidth()) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = food.name,
                                        style = RitimoTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = RitimoTheme.colors.onSurface
                                    )
                                    RitimoTertiaryButton(
                                        text = "Trocar",
                                        onClick = { selectedFood = null }
                                    )
                                }

                                Text(
                                    text = food.source,
                                    style = RitimoTheme.typography.bodySmall,
                                    color = RitimoTheme.colors.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(RitimoTheme.spacing.xs))

                                RitimoStatusBadge(
                                    text = "PORÇÃO: ${calculatedPortion.caloriesKcal} kcal • ${calculatedPortion.proteinGrams}g P • ${calculatedPortion.carbsGrams}g C • ${calculatedPortion.fatGrams}g G",
                                    type = BadgeType.PRIMARY
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.sm)) {
                            RitimoTextField(
                                value = portionAmountStr,
                                onValueChange = { portionAmountStr = it },
                                label = "Quantidade",
                                placeholder = "100",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )

                            RitimoTextField(
                                value = portionUnit,
                                onValueChange = { portionUnit = it },
                                label = "Unidade",
                                placeholder = "g, ml, unidade, colher",
                                modifier = Modifier.weight(1f)
                            )
                        }

                        RitimoTextField(
                            value = portionNotes,
                            onValueChange = { portionNotes = it },
                            label = "Observação (Opcional)",
                            placeholder = "Ex.: Pesado cru"
                        )
                    }
                }
            } else {
                // Tab 1: Custom Food Entry
                Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
                    RitimoTextField(
                        value = customName,
                        onValueChange = { customName = it },
                        label = "Nome do alimento",
                        placeholder = "Ex.: Suplemento manipulado"
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.sm)) {
                        RitimoTextField(
                            value = customAmount,
                            onValueChange = { customAmount = it },
                            label = "Quantidade",
                            placeholder = "100",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )

                        RitimoTextField(
                            value = customUnit,
                            onValueChange = { customUnit = it },
                            label = "Unidade",
                            placeholder = "g, ml, porção",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.sm)) {
                        RitimoTextField(
                            value = customKcal,
                            onValueChange = { customKcal = it },
                            label = "Calorias (kcal)",
                            placeholder = "150",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )

                        RitimoTextField(
                            value = customProtein,
                            onValueChange = { customProtein = it },
                            label = "Proteínas (g)",
                            placeholder = "20",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.sm)) {
                        RitimoTextField(
                            value = customCarbs,
                            onValueChange = { customCarbs = it },
                            label = "Carboidratos (g)",
                            placeholder = "10",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )

                        RitimoTextField(
                            value = customFat,
                            onValueChange = { customFat = it },
                            label = "Gorduras (g)",
                            placeholder = "2",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    RitimoTextField(
                        value = customNotes,
                        onValueChange = { customNotes = it },
                        label = "Observação (Opcional)",
                        placeholder = "Ex.: Tomar com água"
                    )
                }
            }
        },
        confirmButton = {
            if (activeTab == 0) {
                selectedFood?.let { food ->
                    val amountVal = portionAmountStr.toDoubleOrNull() ?: 100.0
                    RitimoPrimaryButton(
                        text = "Adicionar Alimento",
                        onClick = {
                            onConfirmCatalogPortion(food, amountVal, portionUnit.ifBlank { "g" }, portionNotes.takeIf { it.isNotBlank() })
                        }
                    )
                }
            } else {
                RitimoPrimaryButton(
                    text = "Adicionar Personalizado",
                    onClick = {
                        if (customName.isNotBlank() && customAmount.isNotBlank()) {
                            onConfirmCustomFood(
                                customName.trim(),
                                customAmount.trim(),
                                customUnit.trim().ifBlank { "g" },
                                customKcal.toDoubleOrNull() ?: 0.0,
                                customProtein.toDoubleOrNull() ?: 0.0,
                                customCarbs.toDoubleOrNull() ?: 0.0,
                                customFat.toDoubleOrNull() ?: 0.0,
                                customNotes.trim().takeIf { it.isNotEmpty() }
                            )
                        }
                    }
                )
            }
        },
        dismissButton = {
            RitimoTertiaryButton(
                text = "Cancelar",
                onClick = onDismiss
            )
        }
    )
}
