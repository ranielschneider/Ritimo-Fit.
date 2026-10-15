package com.ranielschneider.ritimofit.data.repository

import com.ranielschneider.ritimofit.core.dispatcher.AppDispatchers
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.data.datasource.MealCompletionDataSource
import com.ranielschneider.ritimofit.domain.model.MealCompletion
import com.ranielschneider.ritimofit.domain.repository.MealCompletionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class MealCompletionRepositoryImpl(
    private val mealCompletionDataSource: MealCompletionDataSource,
    private val dispatchers: AppDispatchers
) : MealCompletionRepository {

    override fun getCompletionsForStudent(studentId: String): Flow<AppResult<List<MealCompletion>>> {
        return mealCompletionDataSource.observeCompletionsForStudent(studentId).flowOn(dispatchers.io)
    }

    override suspend fun completeMeal(completion: MealCompletion): AppResult<Unit> {
        return withContext(dispatchers.io) {
            mealCompletionDataSource.completeMeal(completion)
        }
    }

    override suspend fun undoMealCompletion(studentId: String, dietId: String, mealId: String): AppResult<Unit> {
        return withContext(dispatchers.io) {
            mealCompletionDataSource.undoMealCompletion(studentId, dietId, mealId)
        }
    }
}
