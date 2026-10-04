package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Exercise
import com.ranielschneider.ritimofit.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow

class GetExerciseCatalogUseCase(
    private val exerciseRepository: ExerciseRepository
) {
    operator fun invoke(): Flow<AppResult<List<Exercise>>> {
        return exerciseRepository.getCatalogExercises()
    }
}
