package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Diet
import com.ranielschneider.ritimofit.domain.repository.DietRepository

class GetDietDetailUseCase(
    private val dietRepository: DietRepository
) {
    suspend operator fun invoke(dietId: String): AppResult<Diet> {
        return dietRepository.getDietById(dietId)
    }
}
