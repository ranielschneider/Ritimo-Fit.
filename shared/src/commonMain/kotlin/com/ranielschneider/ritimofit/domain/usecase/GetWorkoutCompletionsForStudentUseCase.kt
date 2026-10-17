package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.CompletedWorkoutDetails
import com.ranielschneider.ritimofit.domain.model.WorkoutCompletion
import com.ranielschneider.ritimofit.domain.repository.WorkoutCompletionRepository
import com.ranielschneider.ritimofit.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWorkoutCompletionsForStudentUseCase(
    private val workoutCompletionRepository: WorkoutCompletionRepository,
    private val workoutRepository: WorkoutRepository
) {
    fun observeCompletions(studentId: String): Flow<AppResult<List<WorkoutCompletion>>> {
        return workoutCompletionRepository.getCompletionsForStudent(studentId)
    }

    fun observeCompletedDetails(studentId: String): Flow<AppResult<List<CompletedWorkoutDetails>>> {
        return workoutCompletionRepository.getCompletionsForStudent(studentId).map { result ->
            when (result) {
                is AppResult.Success -> {
                    val detailsList = mutableListOf<CompletedWorkoutDetails>()
                    for (completion in result.data) {
                        when (val workoutResult = workoutRepository.getWorkoutById(completion.workoutId)) {
                            is AppResult.Success -> {
                                detailsList.add(
                                    CompletedWorkoutDetails(
                                        completionId = completion.id,
                                        studentId = studentId,
                                        workout = workoutResult.data,
                                        startedAtEpochMs = completion.startedAtEpochMs,
                                        completedAtEpochMs = completion.completedAtEpochMs,
                                        durationSeconds = completion.durationSeconds
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
