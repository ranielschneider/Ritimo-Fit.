package com.ranielschneider.ritimofit.domain.model

data class WaterTracking(
    val studentId: String,
    val dailyGoalMl: Int = 2500,
    val consumedMl: Int = 0
)
