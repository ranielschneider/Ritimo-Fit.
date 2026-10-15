package com.ranielschneider.ritimofit.domain.model

data class MealCompletion(
    val id: String,
    val studentId: String,
    val dietId: String,
    val mealId: String,
    val completedAtEpochMs: Long = 0L
)
