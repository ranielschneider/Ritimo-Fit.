package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Workout
import com.ranielschneider.ritimofit.domain.repository.AuthRepository
import com.ranielschneider.ritimofit.domain.repository.WorkoutRepository

class SaveWorkoutUseCase(
    private val authRepository: AuthRepository,
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(workout: Workout): AppResult<Unit> {
        val currentUserId = when (val userResult = authRepository.getCurrentSessionUser()) {
            is AppResult.Success -> userResult.data.id
            is AppResult.Error -> "prof_123"
        }

        val updatedWorkout = workout.copy(
            professorId = if (workout.professorId.isBlank()) currentUserId else workout.professorId
        )

        return workoutRepository.saveWorkout(updatedWorkout)
    }
}
