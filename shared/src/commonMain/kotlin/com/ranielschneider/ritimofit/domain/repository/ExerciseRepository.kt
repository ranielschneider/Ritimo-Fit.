package com.ranielschneider.ritimofit.domain.repository

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    fun getCatalogExercises(): Flow<AppResult<List<Exercise>>>
    suspend fun getExerciseById(id: String): AppResult<Exercise>
    suspend fun createCustomExercise(exercise: Exercise): AppResult<Unit>
}
