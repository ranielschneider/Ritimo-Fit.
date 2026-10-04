package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.WorkoutAssignment
import kotlinx.coroutines.flow.Flow

interface WorkoutAssignmentDataSource {
    fun observeAssignmentsForStudent(studentId: String): Flow<AppResult<List<WorkoutAssignment>>>
    suspend fun assignWorkout(assignment: WorkoutAssignment): AppResult<Unit>
    suspend fun removeAssignment(studentId: String, workoutId: String): AppResult<Unit>
}
