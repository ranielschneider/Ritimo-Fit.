package com.ranielschneider.ritimofit.domain.model

data class Progress(
    val id: String,
    val studentId: String,
    val dateEpochMs: Long,
    val weightKg: Double? = null,
    val bodyFatPercentage: Double? = null,
    val notes: String? = null
)
