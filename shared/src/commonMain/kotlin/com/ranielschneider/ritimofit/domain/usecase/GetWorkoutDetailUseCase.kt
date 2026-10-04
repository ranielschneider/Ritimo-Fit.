package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Workout
import com.ranielschneider.ritimofit.domain.repository.WorkoutRepository

class GetWorkoutDetailUseCase(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(workoutId: String): AppResult<Workout> {
        return workoutRepository.getWorkoutById(workoutId)
    }
}
