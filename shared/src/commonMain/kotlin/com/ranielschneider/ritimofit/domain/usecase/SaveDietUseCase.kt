package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Diet
import com.ranielschneider.ritimofit.domain.repository.AuthRepository
import com.ranielschneider.ritimofit.domain.repository.DietRepository

class SaveDietUseCase(
    private val authRepository: AuthRepository,
    private val dietRepository: DietRepository
) {
    suspend operator fun invoke(diet: Diet): AppResult<Unit> {
        val currentUserId = when (val userResult = authRepository.getCurrentSessionUser()) {
            is AppResult.Success -> userResult.data.id
            is AppResult.Error -> "prof_123"
        }

        val updatedDiet = diet.copy(
            professorId = if (diet.professorId.isBlank()) currentUserId else diet.professorId
        )

        return dietRepository.saveDiet(updatedDiet)
    }
}
