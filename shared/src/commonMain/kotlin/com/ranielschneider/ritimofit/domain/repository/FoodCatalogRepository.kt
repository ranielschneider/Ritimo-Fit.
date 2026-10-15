package com.ranielschneider.ritimofit.domain.repository

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Food
import kotlinx.coroutines.flow.Flow

interface FoodCatalogRepository {
    fun searchFoods(query: String): Flow<AppResult<List<Food>>>
    suspend fun getFoodById(id: String): AppResult<Food>
}
