package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.DietAssignment
import com.ranielschneider.ritimofit.domain.repository.DietAssignmentRepository

class AssignDietToStudentUseCase(
    private val dietAssignmentRepository: DietAssignmentRepository
) {
    suspend operator fun invoke(studentId: String, dietId: String): AppResult<Unit> {
        val assignment = DietAssignment(
            id = "dasgn_" + (10000..99999).random() + "_" + dietId,
            studentId = studentId,
            dietId = dietId
        )
        return dietAssignmentRepository.assignDietToStudent(assignment)
    }
}
