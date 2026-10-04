package com.ranielschneider.ritimofit.domain.model

data class Workout(
    val id: String,
    val professorId: String,
    val title: String,
    val description: String? = null,
    val studentId: String? = null,
    val exercises: List<WorkoutExercise> = emptyList(),
    val createdAtEpochMs: Long = 0L
)

data class WorkoutExercise(
    val id: String = "",
    val exerciseId: String,
    val exerciseName: String,
    val muscleGroup: String? = null,
    val equipment: String? = null,
    val order: Int = 0,
    val sets: Int = 4,
    val reps: String = "12",
    val weightKg: Double? = null,
    val restSeconds: Int = 60,
    val notes: String? = null
)
