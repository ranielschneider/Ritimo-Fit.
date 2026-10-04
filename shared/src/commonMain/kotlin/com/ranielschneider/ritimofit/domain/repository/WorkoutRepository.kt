package com.ranielschneider.ritimofit.domain.repository

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getWorkoutsForStudent(studentId: String): Flow<AppResult<List<Workout>>>
    fun getWorkoutsForProfessor(professorId: String): Flow<AppResult<List<Workout>>>
    suspend fun getWorkoutById(workoutId: String): AppResult<Workout>
    suspend fun saveWorkout(workout: Workout): AppResult<Unit>
    suspend fun deleteWorkout(workoutId: String): AppResult<Unit>
}
