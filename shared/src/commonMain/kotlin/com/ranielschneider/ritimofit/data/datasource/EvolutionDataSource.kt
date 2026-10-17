package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.BodyMeasurementRecord
import com.ranielschneider.ritimofit.domain.model.WeightRecord
import kotlinx.coroutines.flow.Flow

interface EvolutionDataSource {
    fun observeWeightRecords(studentId: String): Flow<AppResult<List<WeightRecord>>>
    fun observeMeasurementRecords(studentId: String): Flow<AppResult<List<BodyMeasurementRecord>>>
    suspend fun addWeightRecord(record: WeightRecord): AppResult<Unit>
    suspend fun deleteWeightRecord(studentId: String, recordId: String): AppResult<Unit>
    suspend fun addMeasurementRecord(record: BodyMeasurementRecord): AppResult<Unit>
}
