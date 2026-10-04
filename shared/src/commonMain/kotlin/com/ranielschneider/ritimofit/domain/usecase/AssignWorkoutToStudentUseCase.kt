package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.WorkoutAssignment
import com.ranielschneider.ritimofit.domain.repository.WorkoutAssignmentRepository

class AssignWorkoutToStudentUseCase(
    private val workoutAssignmentRepository: WorkoutAssignmentRepository
) {
    suspend operator fun invoke(studentId: String, workoutId: String): AppResult<Unit> {
        val assignment = WorkoutAssignment(
            id = "asgn_" + (10000..99999).random() + "_" + workoutId,
            studentId = studentId,
            workoutId = workoutId
        )
        return workoutAssignmentRepository.assignWorkoutToStudent(assignment)
    }
}
