package com.ranielschneider.ritimofit.domain.repository

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.MealCompletion
import kotlinx.coroutines.flow.Flow

interface MealCompletionRepository {
    fun getCompletionsForStudent(studentId: String): Flow<AppResult<List<MealCompletion>>>
    suspend fun completeMeal(completion: MealCompletion): AppResult<Unit>
    suspend fun undoMealCompletion(studentId: String, dietId: String, mealId: String): AppResult<Unit>
}
