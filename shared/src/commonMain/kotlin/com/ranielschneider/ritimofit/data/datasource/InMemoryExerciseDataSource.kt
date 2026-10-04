package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppError
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Exercise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryExerciseDataSource : ExerciseDataSource {
    private val exercisesState = MutableStateFlow<List<Exercise>>(
        LocalExerciseCatalog.exercises
    )

    override fun observeExercises(): Flow<AppResult<List<Exercise>>> {
        return MutableStateFlow<AppResult<List<Exercise>>>(AppResult.Success(exercisesState.value)).asStateFlow()
    }

    override suspend fun getExerciseById(id: String): AppResult<Exercise> {
        val exercise = exercisesState.value.find { it.id == id }
        return if (exercise != null) {
            AppResult.Success(exercise)
        } else {
            AppResult.Error(AppError.NotFound("Exercício não encontrado"))
        }
    }

    override suspend fun createCustomExercise(exercise: Exercise): AppResult<Unit> {
        val list = exercisesState.value.toMutableList()
        list.add(exercise.copy(isCustom = true))
        exercisesState.value = list
        return AppResult.Success(Unit)
    }
}
