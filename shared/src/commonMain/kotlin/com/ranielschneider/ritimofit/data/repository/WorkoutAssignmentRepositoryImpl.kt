package com.ranielschneider.ritimofit.data.repository

import com.ranielschneider.ritimofit.core.dispatcher.AppDispatchers
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.data.datasource.WorkoutAssignmentDataSource
import com.ranielschneider.ritimofit.domain.model.WorkoutAssignment
import com.ranielschneider.ritimofit.domain.repository.WorkoutAssignmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class WorkoutAssignmentRepositoryImpl(
    private val workoutAssignmentDataSource: WorkoutAssignmentDataSource,
    private val dispatchers: AppDispatchers
) : WorkoutAssignmentRepository {

    override fun getAssignmentsForStudent(studentId: String): Flow<AppResult<List<WorkoutAssignment>>> {
        return workoutAssignmentDataSource.observeAssignmentsForStudent(studentId).flowOn(dispatchers.io)
    }

    override suspend fun assignWorkoutToStudent(assignment: WorkoutAssignment): AppResult<Unit> {
        return withContext(dispatchers.io) {
            workoutAssignmentDataSource.assignWorkout(assignment)
        }
    }

    override suspend fun removeWorkoutAssignment(studentId: String, workoutId: String): AppResult<Unit> {
        return withContext(dispatchers.io) {
            workoutAssignmentDataSource.removeAssignment(studentId, workoutId)
        }
    }
}
