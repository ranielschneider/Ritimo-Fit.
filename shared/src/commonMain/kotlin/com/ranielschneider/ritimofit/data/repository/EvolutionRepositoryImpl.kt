package com.ranielschneider.ritimofit.data.repository

import com.ranielschneider.ritimofit.core.dispatcher.AppDispatchers
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.data.datasource.EvolutionDataSource
import com.ranielschneider.ritimofit.domain.model.BodyMeasurementRecord
import com.ranielschneider.ritimofit.domain.model.WeightRecord
import com.ranielschneider.ritimofit.domain.repository.EvolutionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class EvolutionRepositoryImpl(
    private val evolutionDataSource: EvolutionDataSource,
    private val dispatchers: AppDispatchers
) : EvolutionRepository {

    override fun getWeightRecords(studentId: String): Flow<AppResult<List<WeightRecord>>> {
        return evolutionDataSource.observeWeightRecords(studentId).flowOn(dispatchers.io)
    }

    override fun getMeasurementRecords(studentId: String): Flow<AppResult<List<BodyMeasurementRecord>>> {
        return evolutionDataSource.observeMeasurementRecords(studentId).flowOn(dispatchers.io)
    }

    override suspend fun addWeightRecord(record: WeightRecord): AppResult<Unit> {
        return withContext(dispatchers.io) {
            evolutionDataSource.addWeightRecord(record)
        }
    }

    override suspend fun deleteWeightRecord(studentId: String, recordId: String): AppResult<Unit> {
        return withContext(dispatchers.io) {
            evolutionDataSource.deleteWeightRecord(studentId, recordId)
        }
    }

    override suspend fun addMeasurementRecord(record: BodyMeasurementRecord): AppResult<Unit> {
        return withContext(dispatchers.io) {
            evolutionDataSource.addMeasurementRecord(record)
        }
    }
}
