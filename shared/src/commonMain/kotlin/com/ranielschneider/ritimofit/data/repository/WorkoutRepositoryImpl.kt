package com.ranielschneider.ritimofit.data.repository

import com.ranielschneider.ritimofit.core.dispatcher.AppDispatchers
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.data.datasource.WorkoutDataSource
import com.ranielschneider.ritimofit.domain.model.Workout
import com.ranielschneider.ritimofit.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class WorkoutRepositoryImpl(
    private val workoutDataSource: WorkoutDataSource,
    private val dispatchers: AppDispatchers
) : WorkoutRepository {

    override fun getWorkoutsForStudent(studentId: String): Flow<AppResult<List<Workout>>> {
        return workoutDataSource.observeWorkoutsForStudent(studentId).flowOn(dispatchers.io)
    }

    override fun getWorkoutsForProfessor(professorId: String): Flow<AppResult<List<Workout>>> {
        return workoutDataSource.observeWorkoutsForProfessor(professorId).flowOn(dispatchers.io)
    }

    override suspend fun getWorkoutById(workoutId: String): AppResult<Workout> {
        return withContext(dispatchers.io) {
            workoutDataSource.getWorkoutById(workoutId)
        }
    }

    override suspend fun saveWorkout(workout: Workout): AppResult<Unit> {
        return withContext(dispatchers.io) {
            workoutDataSource.saveWorkout(workout)
        }
    }

    override suspend fun deleteWorkout(workoutId: String): AppResult<Unit> {
        return withContext(dispatchers.io) {
            workoutDataSource.deleteWorkout(workoutId)
        }
    }
}
