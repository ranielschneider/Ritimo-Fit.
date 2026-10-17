package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.EvolutionSummary
import com.ranielschneider.ritimofit.domain.repository.EvolutionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlin.math.roundToInt

class GetStudentEvolutionUseCase(
    private val evolutionRepository: EvolutionRepository
) {
    operator fun invoke(studentId: String): Flow<AppResult<EvolutionSummary>> {
        return combine(
            evolutionRepository.getWeightRecords(studentId),
            evolutionRepository.getMeasurementRecords(studentId)
        ) { weightsResult, measurementsResult ->
            val weights = if (weightsResult is AppResult.Success) weightsResult.data else emptyList()
            val measurements = if (measurementsResult is AppResult.Success) measurementsResult.data else emptyList()

            val latestWeight = weights.firstOrNull()?.weightKg
            val initialWeight = weights.lastOrNull()?.weightKg

            val weightDiff = if (latestWeight != null && initialWeight != null) {
                ((latestWeight - initialWeight) * 10.0).roundToInt() / 10.0
            } else null

            AppResult.Success(
                EvolutionSummary(
                    latestWeightKg = latestWeight,
                    initialWeightKg = initialWeight,
                    weightDifferenceKg = weightDiff,
                    weightRecords = weights,
                    measurementRecords = measurements
                )
            )
        }
    }
}
