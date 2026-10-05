package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.repository.DietRepository

class DeleteDietUseCase(
    private val dietRepository: DietRepository
) {
    suspend operator fun invoke(dietId: String): AppResult<Unit> {
        return dietRepository.deleteDiet(dietId)
    }
}
