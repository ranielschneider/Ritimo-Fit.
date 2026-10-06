package com.ranielschneider.ritimofit.data.repository

import com.ranielschneider.ritimofit.core.dispatcher.AppDispatchers
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.data.datasource.DietAssignmentDataSource
import com.ranielschneider.ritimofit.domain.model.DietAssignment
import com.ranielschneider.ritimofit.domain.repository.DietAssignmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class DietAssignmentRepositoryImpl(
    private val dietAssignmentDataSource: DietAssignmentDataSource,
    private val dispatchers: AppDispatchers
) : DietAssignmentRepository {

    override fun getAssignmentsForStudent(studentId: String): Flow<AppResult<List<DietAssignment>>> {
        return dietAssignmentDataSource.observeAssignmentsForStudent(studentId).flowOn(dispatchers.io)
    }

    override suspend fun assignDietToStudent(assignment: DietAssignment): AppResult<Unit> {
        return withContext(dispatchers.io) {
            dietAssignmentDataSource.assignDiet(assignment)
        }
    }

    override suspend fun removeDietAssignment(studentId: String, dietId: String): AppResult<Unit> {
        return withContext(dispatchers.io) {
            dietAssignmentDataSource.removeAssignment(studentId, dietId)
        }
    }
}
