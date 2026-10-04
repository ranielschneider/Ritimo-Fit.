package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.repository.WorkoutAssignmentRepository

class RemoveWorkoutAssignmentUseCase(
    private val workoutAssignmentRepository: WorkoutAssignmentRepository
) {
    suspend operator fun invoke(studentId: String, workoutId: String): AppResult<Unit> {
        return workoutAssignmentRepository.removeWorkoutAssignment(studentId, workoutId)
    }
}
