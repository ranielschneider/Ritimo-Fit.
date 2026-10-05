package com.ranielschneider.ritimofit.data.repository

import com.ranielschneider.ritimofit.core.dispatcher.AppDispatchers
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.data.datasource.WaterTrackingDataSource
import com.ranielschneider.ritimofit.domain.model.WaterTracking
import com.ranielschneider.ritimofit.domain.repository.WaterTrackingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class WaterTrackingRepositoryImpl(
    private val waterTrackingDataSource: WaterTrackingDataSource,
    private val dispatchers: AppDispatchers
) : WaterTrackingRepository {

    override fun getWaterTracking(studentId: String): Flow<AppResult<WaterTracking>> {
        return waterTrackingDataSource.observeWaterTracking(studentId).flowOn(dispatchers.io)
    }

    override suspend fun addWater(studentId: String, amountMl: Int): AppResult<Unit> {
        return withContext(dispatchers.io) {
            waterTrackingDataSource.addWater(studentId, amountMl)
        }
    }

    override suspend fun removeWater(studentId: String, amountMl: Int): AppResult<Unit> {
        return withContext(dispatchers.io) {
            waterTrackingDataSource.removeWater(studentId, amountMl)
        }
    }
}
