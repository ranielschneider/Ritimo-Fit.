package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.MealCompletion
import com.ranielschneider.ritimofit.domain.repository.AuthRepository
import com.ranielschneider.ritimofit.domain.repository.MealCompletionRepository

class CompleteMealUseCase(
    private val authRepository: AuthRepository,
    private val mealCompletionRepository: MealCompletionRepository
) {
    suspend operator fun invoke(dietId: String, mealId: String): AppResult<Unit> {
        val currentUserResult = authRepository.getCurrentSessionUser()
        val studentId = when (currentUserResult) {
            is AppResult.Success -> currentUserResult.data.id
            is AppResult.Error -> "std_1"
        }

        val completion = MealCompletion(
            id = "mcmpl_" + (10000..99999).random() + "_" + mealId,
            studentId = studentId,
            dietId = dietId,
            mealId = mealId,
            completedAtEpochMs = 0L
        )

        return mealCompletionRepository.completeMeal(completion)
    }
}
