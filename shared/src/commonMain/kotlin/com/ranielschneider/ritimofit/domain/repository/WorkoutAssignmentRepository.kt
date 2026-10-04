package com.ranielschneider.ritimofit.domain.repository

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.WorkoutAssignment
import kotlinx.coroutines.flow.Flow

interface WorkoutAssignmentRepository {
    fun getAssignmentsForStudent(studentId: String): Flow<AppResult<List<WorkoutAssignment>>>
    suspend fun assignWorkoutToStudent(assignment: WorkoutAssignment): AppResult<Unit>
    suspend fun removeWorkoutAssignment(studentId: String, workoutId: String): AppResult<Unit>
}
