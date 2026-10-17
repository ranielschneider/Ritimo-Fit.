package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.BodyMeasurementRecord
import com.ranielschneider.ritimofit.domain.repository.EvolutionRepository

class AddBodyMeasurementUseCase(
    private val evolutionRepository: EvolutionRepository
) {
    suspend operator fun invoke(
        studentId: String,
        dateEpochMs: Long,
        waistCm: Double? = null,
        abdomenCm: Double? = null,
        chestCm: Double? = null,
        armCm: Double? = null,
        thighCm: Double? = null,
        hipCm: Double? = null,
        notes: String? = null
    ): AppResult<Unit> {
        val record = BodyMeasurementRecord(
            id = "bm_" + (10000..99999).random(),
            studentId = studentId,
            dateEpochMs = dateEpochMs,
            waistCm = waistCm?.takeIf { it > 0 },
            abdomenCm = abdomenCm?.takeIf { it > 0 },
            chestCm = chestCm?.takeIf { it > 0 },
            armCm = armCm?.takeIf { it > 0 },
            thighCm = thighCm?.takeIf { it > 0 },
            hipCm = hipCm?.takeIf { it > 0 },
            notes = notes?.trim()?.takeIf { it.isNotEmpty() }
        )

        return evolutionRepository.addMeasurementRecord(record)
    }
}
