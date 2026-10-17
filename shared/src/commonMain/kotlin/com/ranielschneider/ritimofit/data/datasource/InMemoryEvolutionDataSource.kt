package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.BodyMeasurementRecord
import com.ranielschneider.ritimofit.domain.model.WeightRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class InMemoryEvolutionDataSource : EvolutionDataSource {

    private val weightRecordsState = MutableStateFlow<List<WeightRecord>>(
        listOf(
            WeightRecord(id = "w_1", studentId = "std_1", weightKg = 82.5, dateEpochMs = 1700000000000L, notes = "Início da consultoria"),
            WeightRecord(id = "w_2", studentId = "std_1", weightKg = 80.8, dateEpochMs = 1702500000000L, notes = "Avaliação mês 1"),
            WeightRecord(id = "w_3", studentId = "std_1", weightKg = 79.5, dateEpochMs = 1705000000000L, notes = "Avaliação mês 2")
        )
    )

    private val measurementRecordsState = MutableStateFlow<List<BodyMeasurementRecord>>(
        listOf(
            BodyMeasurementRecord(
                id = "bm_1",
                studentId = "std_1",
                dateEpochMs = 1700000000000L,
                waistCm = 88.0,
                abdomenCm = 92.0,
                chestCm = 102.0,
                armCm = 38.5,
                thighCm = 58.0,
                hipCm = 98.0,
                notes = "Medidas iniciais"
            ),
            BodyMeasurementRecord(
                id = "bm_2",
                studentId = "std_1",
                dateEpochMs = 1705000000000L,
                waistCm = 84.0,
                abdomenCm = 87.5,
                chestCm = 104.0,
                armCm = 39.5,
                thighCm = 59.5,
                hipCm = 96.0,
                notes = "Evolução 60 dias"
            )
        )
    )

    override fun observeWeightRecords(studentId: String): Flow<AppResult<List<WeightRecord>>> {
        return weightRecordsState.map { list ->
            val filtered = list.filter { it.studentId == studentId }.sortedByDescending { it.dateEpochMs }
            AppResult.Success(filtered)
        }
    }

    override fun observeMeasurementRecords(studentId: String): Flow<AppResult<List<BodyMeasurementRecord>>> {
        return measurementRecordsState.map { list ->
            val filtered = list.filter { it.studentId == studentId }.sortedByDescending { it.dateEpochMs }
            AppResult.Success(filtered)
        }
    }

    override suspend fun addWeightRecord(record: WeightRecord): AppResult<Unit> {
        val current = weightRecordsState.value.toMutableList()
        current.add(record)
        weightRecordsState.value = current
        return AppResult.Success(Unit)
    }

    override suspend fun deleteWeightRecord(studentId: String, recordId: String): AppResult<Unit> {
        val current = weightRecordsState.value.toMutableList()
        current.removeAll { it.studentId == studentId && it.id == recordId }
        weightRecordsState.value = current
        return AppResult.Success(Unit)
    }

    override suspend fun addMeasurementRecord(record: BodyMeasurementRecord): AppResult<Unit> {
        val current = measurementRecordsState.value.toMutableList()
        current.add(record)
        measurementRecordsState.value = current
        return AppResult.Success(Unit)
    }
}
