package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppError
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.WorkoutCompletion
import com.ranielschneider.ritimofit.domain.repository.AuthRepository
import com.ranielschneider.ritimofit.domain.repository.WorkoutCompletionRepository

class CompleteWorkoutUseCase(
    private val authRepository: AuthRepository,
    private val workoutCompletionRepository: WorkoutCompletionRepository
) {
    suspend operator fun invoke(workoutId: String, currentCompletions: List<WorkoutCompletion>): AppResult<Unit> {
        val currentUserResult = authRepository.getCurrentSessionUser()
        val studentId = when (currentUserResult) {
            is AppResult.Success -> currentUserResult.data.id
            is AppResult.Error -> "std_1"
        }

        val alreadyCompleted = currentCompletions.any { it.studentId == studentId && it.workoutId == workoutId }
        if (alreadyCompleted) {
            return AppResult.Error(AppError.PermissionDenied("Treino já concluído hoje."))
        }

        val completion = WorkoutCompletion(
            id = "cmpl_" + (10000..99999).random() + "_" + workoutId,
            studentId = studentId,
            workoutId = workoutId,
            completedAtEpochMs = 0L
        )

        return workoutCompletionRepository.completeWorkout(completion)
    }
}
