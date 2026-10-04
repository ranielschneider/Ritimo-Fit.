package com.ranielschneider.ritimofit.domain.model

data class DailyGoal(
    val id: String,
    val studentId: String,
    val dateEpochMs: Long,
    val waterTargetMl: Int,
    val waterConsumedMl: Int = 0,
    val workoutCompleted: Boolean = false
)
