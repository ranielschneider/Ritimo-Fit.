package com.ranielschneider.ritimofit.domain.model

data class WorkoutCompletion(
    val id: String,
    val studentId: String,
    val workoutId: String,
    val startedAtEpochMs: Long = 0L,
    val completedAtEpochMs: Long = 0L,
    val durationSeconds: Long = 0L
)

data class CompletedWorkoutDetails(
    val completionId: String,
    val studentId: String,
    val workout: Workout,
    val startedAtEpochMs: Long = 0L,
    val completedAtEpochMs: Long = 0L,
    val durationSeconds: Long = 0L
)
