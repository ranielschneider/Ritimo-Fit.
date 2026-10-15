package com.ranielschneider.ritimofit.data.repository

import com.ranielschneider.ritimofit.core.dispatcher.AppDispatchers
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.data.datasource.FoodCatalogDataSource
import com.ranielschneider.ritimofit.domain.model.Food
import com.ranielschneider.ritimofit.domain.repository.FoodCatalogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class FoodCatalogRepositoryImpl(
    private val foodCatalogDataSource: FoodCatalogDataSource,
    private val dispatchers: AppDispatchers
) : FoodCatalogRepository {

    override fun searchFoods(query: String): Flow<AppResult<List<Food>>> {
        return foodCatalogDataSource.searchFoods(query).flowOn(dispatchers.io)
    }

    override suspend fun getFoodById(id: String): AppResult<Food> {
        return withContext(dispatchers.io) {
            foodCatalogDataSource.getFoodById(id)
        }
    }
}
