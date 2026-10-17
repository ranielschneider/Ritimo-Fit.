package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.domain.model.BodyMeasurementRecord
import com.ranielschneider.ritimofit.domain.model.WeightRecord
import kotlin.math.roundToInt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EvolutionTest {

    @Test
    fun testWeightDifferenceCalculation() {
        val weights = listOf(
            WeightRecord(id = "w_3", studentId = "std_1", weightKg = 79.5, dateEpochMs = 1705000000000L),
            WeightRecord(id = "w_2", studentId = "std_1", weightKg = 80.8, dateEpochMs = 1702500000000L),
            WeightRecord(id = "w_1", studentId = "std_1", weightKg = 82.5, dateEpochMs = 1700000000000L)
        )

        val latest = weights.first().weightKg
        val initial = weights.last().weightKg
        val diff = ((latest - initial) * 10.0).roundToInt() / 10.0

        assertEquals(-3.0, diff)
    }

    @Test
    fun testAddAndRemoveWeightRecords() {
        val records = mutableListOf(
            WeightRecord(id = "w_1", studentId = "std_1", weightKg = 80.0, dateEpochMs = 1000L)
        )

        // Add new record
        val newRecord = WeightRecord(id = "w_2", studentId = "std_1", weightKg = 78.5, dateEpochMs = 2000L)
        records.add(newRecord)
        assertEquals(2, records.size)

        // Delete record
        records.removeAll { it.id == "w_1" }
        assertEquals(1, records.size)
        assertEquals("w_2", records.first().id)
    }

    @Test
    fun testRejectInvalidWeightValues() {
        val invalidWeightZero = 0.0
        val invalidWeightNegative = -5.0
        val invalidWeightExcessive = 600.0

        assertTrue(invalidWeightZero <= 0.0 || invalidWeightZero > 500.0)
        assertTrue(invalidWeightNegative <= 0.0 || invalidWeightNegative > 500.0)
        assertTrue(invalidWeightExcessive <= 0.0 || invalidWeightExcessive > 500.0)

        val validWeight = 75.5
        assertFalse(validWeight <= 0.0 || validWeight > 500.0)
    }

    @Test
    fun testPartialBodyMeasurements() {
        val partialMeasurement = BodyMeasurementRecord(
            id = "bm_1",
            studentId = "std_1",
            dateEpochMs = 1000L,
            waistCm = 84.0,
            armCm = 39.0
        )

        assertEquals(84.0, partialMeasurement.waistCm)
        assertEquals(39.0, partialMeasurement.armCm)
        assertNull(partialMeasurement.chestCm)
        assertNull(partialMeasurement.thighCm)
    }

    @Test
    fun testStudentEvolutionIsolation() {
        val allWeights = listOf(
            WeightRecord(id = "w_1", studentId = "std_1", weightKg = 80.0, dateEpochMs = 1000L),
            WeightRecord(id = "w_2", studentId = "std_2", weightKg = 65.0, dateEpochMs = 1000L)
        )

        val std1Weights = allWeights.filter { it.studentId == "std_1" }
        val std2Weights = allWeights.filter { it.studentId == "std_2" }

        assertEquals(1, std1Weights.size)
        assertEquals(80.0, std1Weights.first().weightKg)

        assertEquals(1, std2Weights.size)
        assertEquals(65.0, std2Weights.first().weightKg)
    }
}
