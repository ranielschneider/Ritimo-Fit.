package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.WorkoutCompletion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class InMemoryWorkoutCompletionDataSource : WorkoutCompletionDataSource {
    private val completionsState = MutableStateFlow<List<WorkoutCompletion>>(emptyList())

    override fun observeCompletionsForStudent(studentId: String): Flow<AppResult<List<WorkoutCompletion>>> {
        return completionsState.map { list ->
            val filtered = list.filter { it.studentId == studentId }
            AppResult.Success(filtered)
        }
    }

    override suspend fun completeWorkout(completion: WorkoutCompletion): AppResult<Unit> {
        val currentList = completionsState.value.toMutableList()
        currentList.add(completion)
        completionsState.value = currentList
        return AppResult.Success(Unit)
    }
}
