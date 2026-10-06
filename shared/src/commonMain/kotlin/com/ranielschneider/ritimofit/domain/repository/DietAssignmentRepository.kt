package com.ranielschneider.ritimofit.domain.repository

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.DietAssignment
import kotlinx.coroutines.flow.Flow

interface DietAssignmentRepository {
    fun getAssignmentsForStudent(studentId: String): Flow<AppResult<List<DietAssignment>>>
    suspend fun assignDietToStudent(assignment: DietAssignment): AppResult<Unit>
    suspend fun removeDietAssignment(studentId: String, dietId: String): AppResult<Unit>
}
