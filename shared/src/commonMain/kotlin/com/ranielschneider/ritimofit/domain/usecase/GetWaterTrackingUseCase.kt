package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.WaterTracking
import com.ranielschneider.ritimofit.domain.repository.WaterTrackingRepository
import kotlinx.coroutines.flow.Flow

class GetWaterTrackingUseCase(
    private val waterTrackingRepository: WaterTrackingRepository
) {
    operator fun invoke(studentId: String): Flow<AppResult<WaterTracking>> {
        return waterTrackingRepository.getWaterTracking(studentId)
    }
}
