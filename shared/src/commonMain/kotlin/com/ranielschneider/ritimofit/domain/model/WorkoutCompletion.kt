package com.ranielschneider.ritimofit.domain.model

data class WorkoutCompletion(
    val id: String,
    val studentId: String,
    val workoutId: String,
    val completedAtEpochMs: Long
)

data class CompletedWorkoutDetails(
    val completionId: String,
    val studentId: String,
    val workout: Workout,
    val completedAtEpochMs: Long
)
