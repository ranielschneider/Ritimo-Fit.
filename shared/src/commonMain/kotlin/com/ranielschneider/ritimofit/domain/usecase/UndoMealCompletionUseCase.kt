package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.repository.AuthRepository
import com.ranielschneider.ritimofit.domain.repository.MealCompletionRepository

class UndoMealCompletionUseCase(
    private val authRepository: AuthRepository,
    private val mealCompletionRepository: MealCompletionRepository
) {
    suspend operator fun invoke(dietId: String, mealId: String): AppResult<Unit> {
        val currentUserResult = authRepository.getCurrentSessionUser()
        val studentId = when (currentUserResult) {
            is AppResult.Success -> currentUserResult.data.id
            is AppResult.Error -> "std_1"
        }

        return mealCompletionRepository.undoMealCompletion(studentId, dietId, mealId)
    }
}
