package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Workout
import com.ranielschneider.ritimofit.domain.repository.AuthRepository
import com.ranielschneider.ritimofit.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow

class GetWorkoutsForProfessorUseCase(
    private val authRepository: AuthRepository,
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(): Flow<AppResult<List<Workout>>> {
        val currentUserId = when (val userResult = authRepository.getCurrentSessionUser()) {
            is AppResult.Success -> userResult.data.id
            is AppResult.Error -> "prof_123"
        }

        return workoutRepository.getWorkoutsForProfessor(currentUserId)
    }
}
