package com.ranielschneider.ritimofit.data.repository

import com.ranielschneider.ritimofit.core.dispatcher.AppDispatchers
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.data.datasource.WorkoutCompletionDataSource
import com.ranielschneider.ritimofit.domain.model.WorkoutCompletion
import com.ranielschneider.ritimofit.domain.repository.WorkoutCompletionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class WorkoutCompletionRepositoryImpl(
    private val workoutCompletionDataSource: WorkoutCompletionDataSource,
    private val dispatchers: AppDispatchers
) : WorkoutCompletionRepository {

    override fun getCompletionsForStudent(studentId: String): Flow<AppResult<List<WorkoutCompletion>>> {
        return workoutCompletionDataSource.observeCompletionsForStudent(studentId).flowOn(dispatchers.io)
    }

    override suspend fun completeWorkout(completion: WorkoutCompletion): AppResult<Unit> {
        return withContext(dispatchers.io) {
            workoutCompletionDataSource.completeWorkout(completion)
        }
    }
}
