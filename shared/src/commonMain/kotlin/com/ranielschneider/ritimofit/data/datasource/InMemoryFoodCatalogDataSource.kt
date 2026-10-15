package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppError
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Food
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class InMemoryFoodCatalogDataSource : FoodCatalogDataSource {

    private val catalogFoods = listOf(
        Food(id = "fd_1", name = "Arroz branco cozido", source = "TACO / Referência Nacional", caloriesPer100g = 128.0, proteinPer100g = 2.5, carbsPer100g = 28.1, fatPer100g = 0.2),
        Food(id = "fd_2", name = "Arroz integral cozido", source = "TACO / Referência Nacional", caloriesPer100g = 124.0, proteinPer100g = 2.6, carbsPer100g = 25.8, fatPer100g = 1.0),
        Food(id = "fd_3", name = "Feijão carioca cozido", source = "TACO / Referência Nacional", caloriesPer100g = 76.0, proteinPer100g = 4.8, carbsPer100g = 13.6, fatPer100g = 0.5),
        Food(id = "fd_4", name = "Feijão preto cozido", source = "TACO / Referência Nacional", caloriesPer100g = 77.0, proteinPer100g = 4.5, carbsPer100g = 14.0, fatPer100g = 0.5),
        Food(id = "fd_5", name = "Peito de frango grelhado", source = "TACO / Referência Nacional", caloriesPer100g = 159.0, proteinPer100g = 32.0, carbsPer100g = 0.0, fatPer100g = 2.5),
        Food(id = "fd_6", name = "Ovo de galinha cozido", source = "TACO / Referência Nacional", caloriesPer100g = 146.0, proteinPer100g = 13.3, carbsPer100g = 0.6, fatPer100g = 9.5),
        Food(id = "fd_7", name = "Patinho moído grelhado", source = "TACO / Referência Nacional", caloriesPer100g = 219.0, proteinPer100g = 35.9, carbsPer100g = 0.0, fatPer100g = 7.3),
        Food(id = "fd_8", name = "Batata doce cozida", source = "TACO / Referência Nacional", caloriesPer100g = 77.0, proteinPer100g = 0.6, carbsPer100g = 18.4, fatPer100g = 0.1),
        Food(id = "fd_9", name = "Banana nanica", source = "TACO / Referência Nacional", caloriesPer100g = 92.0, proteinPer100g = 1.4, carbsPer100g = 23.8, fatPer100g = 0.1),
        Food(id = "fd_10", name = "Aveia em flocos", source = "TACO / Referência Nacional", caloriesPer100g = 394.0, proteinPer100g = 13.9, carbsPer100g = 66.6, fatPer100g = 8.5),
        Food(id = "fd_11", name = "Pão de fôrma integral", source = "TACO / Referência Nacional", caloriesPer100g = 253.0, proteinPer100g = 9.4, carbsPer100g = 49.9, fatPer100g = 3.7),
        Food(id = "fd_12", name = "Whey Protein Concentrado 80%", source = "Referência Esportiva", caloriesPer100g = 400.0, proteinPer100g = 80.0, carbsPer100g = 6.0, fatPer100g = 6.0),
        Food(id = "fd_13", name = "Azeite de oliva extra virgem", source = "TACO / Referência Nacional", caloriesPer100g = 884.0, proteinPer100g = 0.0, carbsPer100g = 0.0, fatPer100g = 100.0)
    )

    private val foodsState = MutableStateFlow<List<Food>>(catalogFoods)

    override fun searchFoods(query: String): Flow<AppResult<List<Food>>> {
        val trimmed = query.trim()
        return foodsState.map { list ->
            if (trimmed.isEmpty()) {
                AppResult.Success(list)
            } else {
                val filtered = list.filter { it.name.contains(trimmed, ignoreCase = true) }
                AppResult.Success(filtered)
            }
        }
    }

    override suspend fun getFoodById(id: String): AppResult<Food> {
        val food = foodsState.value.find { it.id == id }
        return if (food != null) {
            AppResult.Success(food)
        } else {
            AppResult.Error(AppError.NotFound("Alimento não encontrado"))
        }
    }
}
