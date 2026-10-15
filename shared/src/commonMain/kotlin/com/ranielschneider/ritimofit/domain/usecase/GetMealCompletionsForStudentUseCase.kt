package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.MealCompletion
import com.ranielschneider.ritimofit.domain.repository.MealCompletionRepository
import kotlinx.coroutines.flow.Flow

class GetMealCompletionsForStudentUseCase(
    private val mealCompletionRepository: MealCompletionRepository
) {
    fun observeCompletions(studentId: String): Flow<AppResult<List<MealCompletion>>> {
        return mealCompletionRepository.getCompletionsForStudent(studentId)
    }
}
