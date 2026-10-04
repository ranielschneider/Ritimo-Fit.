package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.AssignedWorkoutDetails
import com.ranielschneider.ritimofit.domain.repository.WorkoutAssignmentRepository
import com.ranielschneider.ritimofit.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetStudentWorkoutAssignmentsUseCase(
    private val workoutAssignmentRepository: WorkoutAssignmentRepository,
    private val workoutRepository: WorkoutRepository
) {
    operator fun invoke(studentId: String): Flow<AppResult<List<AssignedWorkoutDetails>>> {
        return workoutAssignmentRepository.getAssignmentsForStudent(studentId).map { result ->
            when (result) {
                is AppResult.Success -> {
                    val detailsList = mutableListOf<AssignedWorkoutDetails>()
                    for (assignment in result.data) {
                        when (val workoutResult = workoutRepository.getWorkoutById(assignment.workoutId)) {
                            is AppResult.Success -> {
                                detailsList.add(
                                    AssignedWorkoutDetails(
                                        assignmentId = assignment.id,
                                        studentId = studentId,
                                        workout = workoutResult.data
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
