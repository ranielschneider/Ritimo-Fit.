package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.MealCompletion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class InMemoryMealCompletionDataSource : MealCompletionDataSource {
    private val completionsState = MutableStateFlow<List<MealCompletion>>(emptyList())

    override fun observeCompletionsForStudent(studentId: String): Flow<AppResult<List<MealCompletion>>> {
        return completionsState.map { list ->
            val filtered = list.filter { it.studentId == studentId }
            AppResult.Success(filtered)
        }
    }

    override suspend fun completeMeal(completion: MealCompletion): AppResult<Unit> {
        val currentList = completionsState.value.toMutableList()
        val exists = currentList.any { it.studentId == completion.studentId && it.dietId == completion.dietId && it.mealId == completion.mealId }
        if (!exists) {
            currentList.add(completion)
            completionsState.value = currentList
        }
        return AppResult.Success(Unit)
    }

    override suspend fun undoMealCompletion(studentId: String, dietId: String, mealId: String): AppResult<Unit> {
        val currentList = completionsState.value.toMutableList()
        currentList.removeAll { it.studentId == studentId && it.dietId == dietId && it.mealId == mealId }
        completionsState.value = currentList
        return AppResult.Success(Unit)
    }
}
