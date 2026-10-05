package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.WaterTracking
import kotlinx.coroutines.flow.Flow

interface WaterTrackingDataSource {
    fun observeWaterTracking(studentId: String): Flow<AppResult<WaterTracking>>
    suspend fun addWater(studentId: String, amountMl: Int): AppResult<Unit>
    suspend fun removeWater(studentId: String, amountMl: Int): AppResult<Unit>
}
