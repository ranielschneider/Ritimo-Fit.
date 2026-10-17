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
    suspend operator fun invoke(
        workoutId: String,
        startedAtEpochMs: Long,
        completedAtEpochMs: Long
    ): AppResult<WorkoutCompletion> {
        if (startedAtEpochMs <= 0) {
            return AppResult.Error(AppError.PermissionDenied("O treino precisa ser iniciado antes de ser concluído."))
        }

        val currentUserResult = authRepository.getCurrentSessionUser()
        val studentId = when (currentUserResult) {
            is AppResult.Success -> currentUserResult.data.id
            is AppResult.Error -> "std_1"
        }

        val durationSec = ((completedAtEpochMs - startedAtEpochMs) / 1000).coerceAtLeast(1)

        val completion = WorkoutCompletion(
            id = "cmpl_" + (10000..99999).random() + "_" + workoutId,
            studentId = studentId,
            workoutId = workoutId,
            startedAtEpochMs = startedAtEpochMs,
            completedAtEpochMs = completedAtEpochMs,
            durationSeconds = durationSec
        )

        return when (val result = workoutCompletionRepository.completeWorkout(completion)) {
            is AppResult.Success -> AppResult.Success(completion)
            is AppResult.Error -> AppResult.Error(result.error)
        }
    }
}
