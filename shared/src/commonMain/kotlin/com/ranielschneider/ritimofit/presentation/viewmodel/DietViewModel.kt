package com.ranielschneider.ritimofit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.calculator.DietNutritionalSummary
import com.ranielschneider.ritimofit.domain.calculator.NutritionalCalculator
import com.ranielschneider.ritimofit.domain.model.Diet
import com.ranielschneider.ritimofit.domain.model.Food
import com.ranielschneider.ritimofit.domain.model.FoodItem
import com.ranielschneider.ritimofit.domain.model.Meal
import com.ranielschneider.ritimofit.domain.usecase.DeleteDietUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetDietDetailUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetDietsForProfessorUseCase
import com.ranielschneider.ritimofit.domain.usecase.SaveDietUseCase
import com.ranielschneider.ritimofit.domain.usecase.SearchFoodsUseCase
import com.ranielschneider.ritimofit.presentation.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DietViewModel(
    private val getDietsForProfessorUseCase: GetDietsForProfessorUseCase,
    private val saveDietUseCase: SaveDietUseCase,
    private val getDietDetailUseCase: GetDietDetailUseCase,
    private val deleteDietUseCase: DeleteDietUseCase,
    private val searchFoodsUseCase: SearchFoodsUseCase
) : ViewModel() {

    // 1. Diet Library State
    private val _dietsUiState = MutableStateFlow<UiState<List<Diet>>>(UiState.Idle)
    val dietsUiState: StateFlow<UiState<List<Diet>>> = _dietsUiState.asStateFlow()

    // 2. Diet Editor State
    private val _currentDietId = MutableStateFlow<String?>(null)
    val currentDietId: StateFlow<String?> = _currentDietId.asStateFlow()

    private val _dietTitle = MutableStateFlow("")
    val dietTitle: StateFlow<String> = _dietTitle.asStateFlow()

    private val _dietDescription = MutableStateFlow("")
    val dietDescription: StateFlow<String> = _dietDescription.asStateFlow()

    private val _totalCalories = MutableStateFlow("")
    val totalCalories: StateFlow<String> = _totalCalories.asStateFlow()

    private val _proteinGrams = MutableStateFlow("")
    val proteinGrams: StateFlow<String> = _proteinGrams.asStateFlow()

    private val _carbsGrams = MutableStateFlow("")
    val carbsGrams: StateFlow<String> = _carbsGrams.asStateFlow()

    private val _fatsGrams = MutableStateFlow("")
    val fatsGrams: StateFlow<String> = _fatsGrams.asStateFlow()

    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _saveError = MutableStateFlow<String?>(null)
    val saveError: StateFlow<String?> = _saveError.asStateFlow()

    // 3. Food Catalog Search State
    private val _foodSearchQuery = MutableStateFlow("")
    val foodSearchQuery: StateFlow<String> = _foodSearchQuery.asStateFlow()

    private val _foodSearchResults = MutableStateFlow<UiState<List<Food>>>(UiState.Idle)
    val foodSearchResults: StateFlow<UiState<List<Food>>> = _foodSearchResults.asStateFlow()

    init {
        loadDiets()
        searchCatalogFoods("")
    }

    fun loadDiets() {
        viewModelScope.launch {
            _dietsUiState.value = UiState.Loading
            getDietsForProfessorUseCase().collectLatest { result ->
                _dietsUiState.value = when (result) {
                    is AppResult.Success -> UiState.Success(result.data)
                    is AppResult.Error -> UiState.Error(result.error)
                }
            }
        }
    }

    fun searchCatalogFoods(query: String) {
        _foodSearchQuery.value = query
        viewModelScope.launch {
            _foodSearchResults.value = UiState.Loading
            searchFoodsUseCase(query).collectLatest { result ->
                _foodSearchResults.value = when (result) {
                    is AppResult.Success -> UiState.Success(result.data)
                    is AppResult.Error -> UiState.Error(result.error)
                }
            }
        }
    }

    fun initNewDiet() {
        _currentDietId.value = null
        _dietTitle.value = ""
        _dietDescription.value = ""
        _totalCalories.value = ""
        _proteinGrams.value = ""
        _carbsGrams.value = ""
        _fatsGrams.value = ""
        _saveError.value = null
        _isSaving.value = false

        _meals.value = listOf(
            Meal(id = "m_1", name = "Café da manhã", time = "08:00", order = 0),
            Meal(id = "m_2", name = "Almoço", time = "12:30", order = 1),
            Meal(id = "m_3", name = "Lanche da tarde", time = "16:00", order = 2),
            Meal(id = "m_4", name = "Jantar", time = "20:00", order = 3),
            Meal(id = "m_5", name = "Ceia", time = "22:00", order = 4)
        )
    }

    fun loadDietForEditing(dietId: String) {
        viewModelScope.launch {
            _isSaving.value = true
            when (val result = getDietDetailUseCase(dietId)) {
                is AppResult.Success -> {
                    val diet = result.data
                    _currentDietId.value = diet.id
                    _dietTitle.value = diet.title
                    _dietDescription.value = diet.description.orEmpty()
                    _totalCalories.value = diet.totalCalories?.toString().orEmpty()
                    _proteinGrams.value = diet.proteinGrams?.toString().orEmpty()
                    _carbsGrams.value = diet.carbsGrams?.toString().orEmpty()
                    _fatsGrams.value = diet.fatsGrams?.toString().orEmpty()
                    _meals.value = diet.meals
                    _saveError.value = null
                }
                is AppResult.Error -> {
                    _saveError.value = "Não foi possível carregar o plano alimentar."
                }
            }
            _isSaving.value = false
        }
    }

    fun onTitleChanged(title: String) {
        _dietTitle.value = title
        _saveError.value = null
    }

    fun onDescriptionChanged(description: String) {
        _dietDescription.value = description
    }

    fun onTotalCaloriesChanged(calories: String) {
        _totalCalories.value = calories
    }

    fun onProteinGramsChanged(protein: String) {
        _proteinGrams.value = protein
    }

    fun onCarbsGramsChanged(carbs: String) {
        _carbsGrams.value = carbs
    }

    fun onFatsGramsChanged(fats: String) {
        _fatsGrams.value = fats
    }

    fun getCalculatedDietSummary(): DietNutritionalSummary {
        return NutritionalCalculator.sumDietMacros(_meals.value)
    }

    fun addMeal(name: String, time: String?) {
        val currentList = _meals.value.toMutableList()
        val nextOrder = currentList.size
        val newMeal = Meal(
            id = "m_" + (1000..9999).random(),
            name = name,
            time = time.takeIf { !it.isNullOrBlank() },
            order = nextOrder
        )
        currentList.add(newMeal)
        _meals.value = currentList
    }

    fun removeMeal(mealIndex: Int) {
        val currentList = _meals.value.toMutableList()
        if (mealIndex in currentList.indices) {
            currentList.removeAt(mealIndex)
            _meals.value = currentList.mapIndexed { i, m -> m.copy(order = i) }
        }
    }

    fun addFoodPortionToMeal(
        mealIndex: Int,
        food: Food,
        amountValue: Double,
        unit: String,
        notes: String? = null
    ) {
        val currentList = _meals.value.toMutableList()
        if (mealIndex in currentList.indices) {
            val meal = currentList[mealIndex]
            val updatedFoods = meal.foods.toMutableList()
            val newFoodItem = NutritionalCalculator.calculatePortion(
                food = food,
                amountValue = amountValue,
                unit = unit,
                notes = notes
            )
            updatedFoods.add(newFoodItem)
            currentList[mealIndex] = meal.copy(foods = updatedFoods)
            _meals.value = currentList
        }
    }

    fun addCustomFoodToMeal(
        mealIndex: Int,
        foodName: String,
        amount: String,
        unit: String,
        caloriesKcal: Double,
        proteinGrams: Double,
        carbsGrams: Double,
        fatGrams: Double,
        notes: String?
    ) {
        val currentList = _meals.value.toMutableList()
        if (mealIndex in currentList.indices) {
            val meal = currentList[mealIndex]
            val updatedFoods = meal.foods.toMutableList()
            val newFood = FoodItem(
                id = "f_" + (1000..9999).random(),
                name = foodName.trim(),
                amount = amount.trim(),
                unit = unit.trim(),
                caloriesKcal = caloriesKcal,
                proteinGrams = proteinGrams,
                carbsGrams = carbsGrams,
                fatGrams = fatGrams,
                isCustom = true,
                notes = notes?.trim()?.takeIf { it.isNotEmpty() }
            )
            updatedFoods.add(newFood)
            currentList[mealIndex] = meal.copy(foods = updatedFoods)
            _meals.value = currentList
        }
    }

    fun removeFoodFromMeal(mealIndex: Int, foodIndex: Int) {
        val currentList = _meals.value.toMutableList()
        if (mealIndex in currentList.indices) {
            val meal = currentList[mealIndex]
            val updatedFoods = meal.foods.toMutableList()
            if (foodIndex in updatedFoods.indices) {
                updatedFoods.removeAt(foodIndex)
                currentList[mealIndex] = meal.copy(foods = updatedFoods)
                _meals.value = currentList
            }
        }
    }

    fun saveDiet(onSuccess: () -> Unit) {
        val title = _dietTitle.value.trim()
        if (title.isEmpty()) {
            _saveError.value = "O nome do plano alimentar é obrigatório."
            return
        }

        viewModelScope.launch {
            _isSaving.value = true
            _saveError.value = null

            val id = _currentDietId.value ?: ("dt_" + (1000..9999).random())
            val diet = Diet(
                id = id,
                professorId = "", // Will be filled by SaveDietUseCase from current session
                title = title,
                description = _dietDescription.value.trim().takeIf { it.isNotEmpty() },
                totalCalories = _totalCalories.value.toIntOrNull(),
                proteinGrams = _proteinGrams.value.toIntOrNull(),
                carbsGrams = _carbsGrams.value.toIntOrNull(),
                fatsGrams = _fatsGrams.value.toIntOrNull(),
                meals = _meals.value,
                createdAtEpochMs = 0L
            )

            when (saveDietUseCase(diet)) {
                is AppResult.Success -> {
                    _isSaving.value = false
                    loadDiets()
                    onSuccess()
                }
                is AppResult.Error -> {
                    _isSaving.value = false
                    _saveError.value = "Erro ao salvar plano alimentar. Tente novamente."
                }
            }
        }
    }

    fun deleteDiet(dietId: String) {
        viewModelScope.launch {
            when (deleteDietUseCase(dietId)) {
                is AppResult.Success -> {
                    loadDiets()
                }
                is AppResult.Error -> {}
            }
        }
    }
}
