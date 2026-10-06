package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.AssignedDietDetails
import com.ranielschneider.ritimofit.domain.repository.DietAssignmentRepository
import com.ranielschneider.ritimofit.domain.repository.DietRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetStudentDietAssignmentsUseCase(
    private val dietAssignmentRepository: DietAssignmentRepository,
    private val dietRepository: DietRepository
) {
    operator fun invoke(studentId: String): Flow<AppResult<List<AssignedDietDetails>>> {
        return dietAssignmentRepository.getAssignmentsForStudent(studentId).map { result ->
            when (result) {
                is AppResult.Success -> {
                    val detailsList = mutableListOf<AssignedDietDetails>()
                    for (assignment in result.data) {
                        when (val dietResult = dietRepository.getDietById(assignment.dietId)) {
                            is AppResult.Success -> {
                                detailsList.add(
                                    AssignedDietDetails(
                                        assignmentId = assignment.id,
                                        studentId = studentId,
                                        diet = dietResult.data
                                    )
                                )
                            }
                            is AppResult.Error -> {}
                        }
                    }
                    AppResult.Success(detailsList)
                }
                is AppResult.Error -> AppResult.Error(result.error)
            }
        }
    }
}
