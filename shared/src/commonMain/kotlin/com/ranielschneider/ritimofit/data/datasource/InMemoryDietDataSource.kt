package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppError
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Diet
import com.ranielschneider.ritimofit.domain.model.FoodItem
import com.ranielschneider.ritimofit.domain.model.Meal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class InMemoryDietDataSource : DietDataSource {
    private val dietsState = MutableStateFlow<List<Diet>>(
        listOf(
            Diet(
                id = "dt_hipertrofia",
                professorId = "prof_123",
                title = "Plano de Hipertrofia Limpa",
                description = "Dieta hipercalórica moderada com foco em aporte proteico elevado.",
                totalCalories = 2600,
                proteinGrams = 180,
                carbsGrams = 320,
                fatsGrams = 70,
                meals = listOf(
                    Meal(
                        id = "m_1",
                        name = "Café da manhã",
                        time = "08:00",
                        order = 0,
                        foods = listOf(
                            FoodItem(id = "f_1", foodId = "fd_6", name = "Ovo mexido", amount = "3", unit = "unidades", caloriesKcal = 219.0, proteinGrams = 19.95, carbsGrams = 0.9, fatGrams = 14.25),
                            FoodItem(id = "f_2", foodId = "fd_11", name = "Pão integral", amount = "2", unit = "fatias", caloriesKcal = 126.5, proteinGrams = 4.7, carbsGrams = 24.95, fatGrams = 1.85),
                            FoodItem(id = "f_3", name = "Café preto sem açúcar", amount = "200", unit = "ml", caloriesKcal = 4.0, proteinGrams = 0.2, carbsGrams = 0.6, fatGrams = 0.0)
                        )
                    ),
                    Meal(
                        id = "m_2",
                        name = "Almoço",
                        time = "12:30",
                        order = 1,
                        foods = listOf(
                            FoodItem(id = "f_4", foodId = "fd_5", name = "Peito de frango grelhado", amount = "180", unit = "g", caloriesKcal = 286.2, proteinGrams = 57.6, carbsGrams = 0.0, fatGrams = 4.5),
                            FoodItem(id = "f_5", foodId = "fd_2", name = "Arroz integral cozido", amount = "200", unit = "g", caloriesKcal = 248.0, proteinGrams = 5.2, carbsGrams = 51.6, fatGrams = 2.0),
                            FoodItem(id = "f_6", foodId = "fd_3", name = "Feijão carioca", amount = "100", unit = "g", caloriesKcal = 76.0, proteinGrams = 4.8, carbsGrams = 13.6, fatGrams = 0.5),
                            FoodItem(id = "f_7", name = "Salada de folhas verdes", amount = "1", unit = "porção", caloriesKcal = 15.0, proteinGrams = 1.0, carbsGrams = 3.0, fatGrams = 0.2)
                        )
                    ),
                    Meal(
                        id = "m_3",
                        name = "Lanche da tarde",
                        time = "16:00",
                        order = 2,
                        foods = listOf(
                            FoodItem(id = "f_8", foodId = "fd_12", name = "Whey Protein Concentrado", amount = "30", unit = "g", caloriesKcal = 120.0, proteinGrams = 24.0, carbsGrams = 1.8, fatGrams = 1.8),
                            FoodItem(id = "f_9", foodId = "fd_9", name = "Banana nanica", amount = "1", unit = "unidade", caloriesKcal = 92.0, proteinGrams = 1.4, carbsGrams = 23.8, fatGrams = 0.1),
                            FoodItem(id = "f_10", foodId = "fd_10", name = "Aveia em flocos", amount = "40", unit = "g", caloriesKcal = 157.6, proteinGrams = 5.56, carbsGrams = 26.64, fatGrams = 3.4)
                        )
                    ),
                    Meal(
                        id = "m_4",
                        name = "Jantar",
                        time = "20:00",
                        order = 3,
                        foods = listOf(
                            FoodItem(id = "f_11", foodId = "fd_7", name = "Patinho moído grelhado", amount = "180", unit = "g", caloriesKcal = 394.2, proteinGrams = 64.62, carbsGrams = 0.0, fatGrams = 13.14),
                            FoodItem(id = "f_12", foodId = "fd_8", name = "Batata doce cozida", amount = "200", unit = "g", caloriesKcal = 154.0, proteinGrams = 1.2, carbsGrams = 36.8, fatGrams = 0.2),
                            FoodItem(id = "f_13", foodId = "fd_13", name = "Azeite de oliva extra virgem", amount = "1", unit = "colher", caloriesKcal = 132.6, proteinGrams = 0.0, carbsGrams = 0.0, fatGrams = 15.0)
                        )
                    )
                )
            )
        )
    )

    override fun observeDietsForProfessor(professorId: String): Flow<AppResult<List<Diet>>> {
        return dietsState.map { list ->
            val filtered = list.filter { it.professorId == professorId }
            AppResult.Success(filtered)
        }
    }

    override suspend fun getDietById(dietId: String): AppResult<Diet> {
        val diet = dietsState.value.find { it.id == dietId }
        return if (diet != null) {
            AppResult.Success(diet)
        } else {
            AppResult.Error(AppError.NotFound("Dieta não encontrada"))
        }
    }

    override suspend fun saveDiet(diet: Diet): AppResult<Unit> {
        val currentList = dietsState.value.toMutableList()
        currentList.removeAll { it.id == diet.id }
        currentList.add(diet)
        dietsState.value = currentList
        return AppResult.Success(Unit)
    }

    override suspend fun deleteDiet(dietId: String): AppResult<Unit> {
        val currentList = dietsState.value.toMutableList()
        currentList.removeAll { it.id == dietId }
        dietsState.value = currentList
        return AppResult.Success(Unit)
    }
}
