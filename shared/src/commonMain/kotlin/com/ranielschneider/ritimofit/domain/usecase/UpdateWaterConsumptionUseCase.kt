package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.repository.WaterTrackingRepository

class UpdateWaterConsumptionUseCase(
    private val waterTrackingRepository: WaterTrackingRepository
) {
    suspend fun addWater(studentId: String, amountMl: Int): AppResult<Unit> {
        return waterTrackingRepository.addWater(studentId, amountMl)
    }

    suspend fun removeWater(studentId: String, amountMl: Int): AppResult<Unit> {
        return waterTrackingRepository.removeWater(studentId, amountMl)
    }
}
