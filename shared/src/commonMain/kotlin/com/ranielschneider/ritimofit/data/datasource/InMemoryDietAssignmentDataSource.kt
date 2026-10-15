package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.DietAssignment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class InMemoryDietAssignmentDataSource : DietAssignmentDataSource {
    private val assignmentsState = MutableStateFlow<List<DietAssignment>>(
        listOf(
            DietAssignment(
                id = "dasgn_1",
                studentId = "std_1",
                dietId = "dt_hipertrofia"
            )
        )
    )

    override fun observeAssignmentsForStudent(studentId: String): Flow<AppResult<List<DietAssignment>>> {
        return assignmentsState.map { list ->
            val filtered = list.filter { it.studentId == studentId }
            AppResult.Success(filtered)
        }
    }

    override suspend fun assignDiet(assignment: DietAssignment): AppResult<Unit> {
        val currentList = assignmentsState.value.toMutableList()
        val exists = currentList.any { it.studentId == assignment.studentId && it.dietId == assignment.dietId }
        if (!exists) {
            currentList.add(assignment)
            assignmentsState.value = currentList
        }
        return AppResult.Success(Unit)
    }

    override suspend fun removeAssignment(studentId: String, dietId: String): AppResult<Unit> {
        val currentList = assignmentsState.value.toMutableList()
        currentList.removeAll { it.studentId == studentId && it.dietId == dietId }
        assignmentsState.value = currentList
        return AppResult.Success(Unit)
    }
}
