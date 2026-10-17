package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.repository.EvolutionRepository

class DeleteWeightRecordUseCase(
    private val evolutionRepository: EvolutionRepository
) {
    suspend operator fun invoke(studentId: String, recordId: String): AppResult<Unit> {
        return evolutionRepository.deleteWeightRecord(studentId, recordId)
    }
}
