package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppError
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Diet
import com.ranielschneider.ritimofit.domain.model.FoodItem
import com.ranielschneider.ritimofit.domain.model.Meal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
                            FoodItem(id = "f_1", name = "Ovo mexido", amount = "3", unit = "unidades"),
                            FoodItem(id = "f_2", name = "Pão integral", amount = "2", unit = "fatias"),
                            FoodItem(id = "f_3", name = "Café preto sem açúcar", amount = "200", unit = "ml")
                        )
                    ),
                    Meal(
                        id = "m_2",
                        name = "Almoço",
                        time = "12:30",
                        order = 1,
                        foods = listOf(
                            FoodItem(id = "f_4", name = "Peito de frango grelhado", amount = "180", unit = "g"),
                            FoodItem(id = "f_5", name = "Arroz integral cozido", amount = "200", unit = "g"),
                            FoodItem(id = "f_6", name = "Feijão carioca", amount = "100", unit = "g"),
                            FoodItem(id = "f_7", name = "Salada de folhas verdes", amount = "1", unit = "porção")
                        )
                    ),
                    Meal(
                        id = "m_3",
                        name = "Lanche da tarde",
                        time = "16:00",
                        order = 2,
                        foods = listOf(
                            FoodItem(id = "f_8", name = "Whey Protein Concentrado", amount = "30", unit = "g"),
                            FoodItem(id = "f_9", name = "Banana nanica", amount = "1", unit = "unidade"),
                            FoodItem(id = "f_10", name = "Aveia em flocos", amount = "40", unit = "g")
                        )
                    ),
                    Meal(
                        id = "m_4",
                        name = "Jantar",
                        time = "20:00",
                        order = 3,
                        foods = listOf(
                            FoodItem(id = "f_11", name = "Patinho moído grelhado", amount = "180", unit = "g"),
                            FoodItem(id = "f_12", name = "Batata doce cozida", amount = "200", unit = "g"),
                            FoodItem(id = "f_13", name = "Azeite de oliva extra virgem", amount = "1", unit = "colher")
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
