package com.ranielschneider.ritimofit.presentation.professor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.ranielschneider.ritimofit.domain.model.Meal
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
                        text = "Metas Nutricionais (Opcionais)",
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
                AddFoodDialog(
                    mealName = meal.name,
                    onDismiss = { activeMealIndexForFoodDialog = null },
                    onConfirm = { foodName, amount, unit, notes ->
                        viewModel.addFoodToMeal(
                            mealIndex = mealIndex,
                            foodName = foodName,
                            amount = amount,
                            unit = unit,
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
private fun AddFoodDialog(
    mealName: String,
    onDismiss: () -> Unit,
    onConfirm: (foodName: String, amount: String, unit: String, notes: String?) -> Unit
) {
    var foodName by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("g") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Adicionar Alimento em $mealName",
                style = RitimoTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.md)) {
                RitimoTextField(
                    value = foodName,
                    onValueChange = { foodName = it },
                    label = "Nome do alimento",
                    placeholder = "Ex.: Peito de frango grelhado"
                )

                Row(horizontalArrangement = Arrangement.spacedBy(RitimoTheme.spacing.sm)) {
                    RitimoTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = "Quantidade",
                        placeholder = "Ex.: 150",
                        modifier = Modifier.weight(1f)
                    )

                    RitimoTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = "Unidade",
                        placeholder = "g, ml, colher",
                        modifier = Modifier.weight(1f)
                    )
                }

                RitimoTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = "Observação (Opcional)",
                    placeholder = "Ex.: Pesado cru"
                )
            }
        },
        confirmButton = {
            RitimoPrimaryButton(
                text = "Adicionar Alimento",
                onClick = {
                    if (foodName.isNotBlank() && amount.isNotBlank()) {
                        onConfirm(foodName.trim(), amount.trim(), unit.trim(), notes.trim().takeIf { it.isNotEmpty() })
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
