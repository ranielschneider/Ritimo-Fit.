package com.ranielschneider.ritimofit.domain.model

data class WeightRecord(
    val id: String,
    val studentId: String,
    val weightKg: Double,
    val dateEpochMs: Long,
    val notes: String? = null
)

data class BodyMeasurementRecord(
    val id: String,
    val studentId: String,
    val dateEpochMs: Long,
    val waistCm: Double? = null,
    val abdomenCm: Double? = null,
    val chestCm: Double? = null,
    val armCm: Double? = null,
    val thighCm: Double? = null,
    val hipCm: Double? = null,
    val notes: String? = null
)

data class EvolutionSummary(
    val latestWeightKg: Double? = null,
    val initialWeightKg: Double? = null,
    val weightDifferenceKg: Double? = null,
    val weightRecords: List<WeightRecord> = emptyList(),
    val measurementRecords: List<BodyMeasurementRecord> = emptyList()
)
