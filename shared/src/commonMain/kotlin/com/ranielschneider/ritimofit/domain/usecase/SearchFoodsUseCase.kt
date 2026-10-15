package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Food
import com.ranielschneider.ritimofit.domain.repository.FoodCatalogRepository
import kotlinx.coroutines.flow.Flow

class SearchFoodsUseCase(
    private val foodCatalogRepository: FoodCatalogRepository
) {
    operator fun invoke(query: String): Flow<AppResult<List<Food>>> {
        return foodCatalogRepository.searchFoods(query)
    }
}
