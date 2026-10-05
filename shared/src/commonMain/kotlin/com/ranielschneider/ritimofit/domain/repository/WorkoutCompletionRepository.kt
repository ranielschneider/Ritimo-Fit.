package com.ranielschneider.ritimofit.domain.repository

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.WorkoutCompletion
import kotlinx.coroutines.flow.Flow

interface WorkoutCompletionRepository {
    fun getCompletionsForStudent(studentId: String): Flow<AppResult<List<WorkoutCompletion>>>
    suspend fun completeWorkout(completion: WorkoutCompletion): AppResult<Unit>
}
