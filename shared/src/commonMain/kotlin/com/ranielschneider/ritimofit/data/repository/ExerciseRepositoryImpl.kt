package com.ranielschneider.ritimofit.data.repository

import com.ranielschneider.ritimofit.core.dispatcher.AppDispatchers
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.data.datasource.ExerciseDataSource
import com.ranielschneider.ritimofit.domain.model.Exercise
import com.ranielschneider.ritimofit.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class ExerciseRepositoryImpl(
    private val exerciseDataSource: ExerciseDataSource,
    private val dispatchers: AppDispatchers
) : ExerciseRepository {

    override fun getCatalogExercises(): Flow<AppResult<List<Exercise>>> {
        return exerciseDataSource.observeExercises().flowOn(dispatchers.io)
    }

    override suspend fun getExerciseById(id: String): AppResult<Exercise> {
        return withContext(dispatchers.io) {
            exerciseDataSource.getExerciseById(id)
        }
    }

    override suspend fun createCustomExercise(exercise: Exercise): AppResult<Unit> {
        return withContext(dispatchers.io) {
            exerciseDataSource.createCustomExercise(exercise)
        }
    }
}
