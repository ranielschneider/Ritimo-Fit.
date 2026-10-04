package com.ranielschneider.ritimofit.domain.model

data class WorkoutAssignment(
    val id: String,
    val studentId: String,
    val workoutId: String,
    val assignedAtEpochMs: Long = 0L
)

data class AssignedWorkoutDetails(
    val assignmentId: String,
    val studentId: String,
    val workout: Workout
)
