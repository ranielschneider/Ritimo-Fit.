package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.WaterTracking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class InMemoryWaterTrackingDataSource : WaterTrackingDataSource {
    private val waterMapState = MutableStateFlow<Map<String, WaterTracking>>(
        mapOf(
            "std_1" to WaterTracking(studentId = "std_1", dailyGoalMl = 2500, consumedMl = 0),
            "std_2" to WaterTracking(studentId = "std_2", dailyGoalMl = 2000, consumedMl = 0)
        )
    )

    override fun observeWaterTracking(studentId: String): Flow<AppResult<WaterTracking>> {
        return waterMapState.map { map ->
            val tracking = map[studentId] ?: WaterTracking(studentId = studentId)
            AppResult.Success(tracking)
        }
    }

    override suspend fun addWater(studentId: String, amountMl: Int): AppResult<Unit> {
        val currentMap = waterMapState.value.toMutableMap()
        val current = currentMap[studentId] ?: WaterTracking(studentId = studentId)
        val newConsumed = (current.consumedMl + amountMl).coerceAtMost(10000)
        currentMap[studentId] = current.copy(consumedMl = newConsumed)
        waterMapState.value = currentMap
        return AppResult.Success(Unit)
    }

    override suspend fun removeWater(studentId: String, amountMl: Int): AppResult<Unit> {
        val currentMap = waterMapState.value.toMutableMap()
        val current = currentMap[studentId] ?: WaterTracking(studentId = studentId)
        val newConsumed = (current.consumedMl - amountMl).coerceAtLeast(0)
        currentMap[studentId] = current.copy(consumedMl = newConsumed)
        waterMapState.value = currentMap
        return AppResult.Success(Unit)
    }
}
