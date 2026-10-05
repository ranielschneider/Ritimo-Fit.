package com.ranielschneider.ritimofit.domain.repository

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.WaterTracking
import kotlinx.coroutines.flow.Flow

interface WaterTrackingRepository {
    fun getWaterTracking(studentId: String): Flow<AppResult<WaterTracking>>
    suspend fun addWater(studentId: String, amountMl: Int): AppResult<Unit>
    suspend fun removeWater(studentId: String, amountMl: Int): AppResult<Unit>
}
