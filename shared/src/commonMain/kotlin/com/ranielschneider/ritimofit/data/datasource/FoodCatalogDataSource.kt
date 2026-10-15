package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Food
import kotlinx.coroutines.flow.Flow

interface FoodCatalogDataSource {
    fun searchFoods(query: String): Flow<AppResult<List<Food>>>
    suspend fun getFoodById(id: String): AppResult<Food>
}
