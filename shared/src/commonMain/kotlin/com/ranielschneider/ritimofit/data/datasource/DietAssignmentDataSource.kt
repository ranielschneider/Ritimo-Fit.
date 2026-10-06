package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.DietAssignment
import kotlinx.coroutines.flow.Flow

interface DietAssignmentDataSource {
    fun observeAssignmentsForStudent(studentId: String): Flow<AppResult<List<DietAssignment>>>
    suspend fun assignDiet(assignment: DietAssignment): AppResult<Unit>
    suspend fun removeAssignment(studentId: String, dietId: String): AppResult<Unit>
}
