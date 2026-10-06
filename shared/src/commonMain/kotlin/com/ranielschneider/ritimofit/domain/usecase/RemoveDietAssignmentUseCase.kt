package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.repository.DietAssignmentRepository

class RemoveDietAssignmentUseCase(
    private val dietAssignmentRepository: DietAssignmentRepository
) {
    suspend operator fun invoke(studentId: String, dietId: String): AppResult<Unit> {
        return dietAssignmentRepository.removeDietAssignment(studentId, dietId)
    }
}
