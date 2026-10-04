package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.WorkoutAssignment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class InMemoryWorkoutAssignmentDataSource : WorkoutAssignmentDataSource {
    private val assignmentsState = MutableStateFlow<List<WorkoutAssignment>>(
        listOf(
            WorkoutAssignment(
                id = "asgn_1",
                studentId = "std_1",
                workoutId = "wk_peito_triceps"
            ),
            WorkoutAssignment(
                id = "asgn_2",
                studentId = "std_1",
                workoutId = "wk_costas_biceps"
            )
        )
    )

    override fun observeAssignmentsForStudent(studentId: String): Flow<AppResult<List<WorkoutAssignment>>> {
        return assignmentsState.map { list ->
            val filtered = list.filter { it.studentId == studentId }
            AppResult.Success(filtered)
        }
    }

    override suspend fun assignWorkout(assignment: WorkoutAssignment): AppResult<Unit> {
        val currentList = assignmentsState.value.toMutableList()
        val exists = currentList.any { it.studentId == assignment.studentId && it.workoutId == assignment.workoutId }
        if (!exists) {
            currentList.add(assignment)
            assignmentsState.value = currentList
        }
        return AppResult.Success(Unit)
    }

    override suspend fun removeAssignment(studentId: String, workoutId: String): AppResult<Unit> {
        val currentList = assignmentsState.value.toMutableList()
        currentList.removeAll { it.studentId == studentId && it.workoutId == workoutId }
        assignmentsState.value = currentList
        return AppResult.Success(Unit)
    }
}
