package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.WorkoutCompletion
import kotlinx.coroutines.flow.Flow

interface WorkoutCompletionDataSource {
    fun observeCompletionsForStudent(studentId: String): Flow<AppResult<List<WorkoutCompletion>>>
    suspend fun completeWorkout(completion: WorkoutCompletion): AppResult<Unit>
}
