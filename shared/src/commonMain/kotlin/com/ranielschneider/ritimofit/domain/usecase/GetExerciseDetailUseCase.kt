package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Exercise
import com.ranielschneider.ritimofit.domain.repository.ExerciseRepository

class GetExerciseDetailUseCase(
    private val exerciseRepository: ExerciseRepository
) {
    suspend operator fun invoke(exerciseId: String): AppResult<Exercise> {
        return exerciseRepository.getExerciseById(exerciseId)
    }
}
