package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppError
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.WeightRecord
import com.ranielschneider.ritimofit.domain.repository.EvolutionRepository

class AddWeightRecordUseCase(
    private val evolutionRepository: EvolutionRepository
) {
    suspend operator fun invoke(
        studentId: String,
        weightKg: Double,
        dateEpochMs: Long,
        notes: String? = null
    ): AppResult<Unit> {
        if (weightKg <= 0.0 || weightKg > 500.0) {
            return AppResult.Error(AppError.PermissionDenied("O peso deve ser um valor positivo válido em kg."))
        }

        val record = WeightRecord(
            id = "w_" + (10000..99999).random(),
            studentId = studentId,
            weightKg = weightKg,
            dateEpochMs = dateEpochMs,
            notes = notes?.trim()?.takeIf { it.isNotEmpty() }
        )

        return evolutionRepository.addWeightRecord(record)
    }
}
