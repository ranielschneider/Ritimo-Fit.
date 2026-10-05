package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Diet
import com.ranielschneider.ritimofit.domain.repository.AuthRepository
import com.ranielschneider.ritimofit.domain.repository.DietRepository
import kotlinx.coroutines.flow.Flow

class GetDietsForProfessorUseCase(
    private val authRepository: AuthRepository,
    private val dietRepository: DietRepository
) {
    suspend operator fun invoke(): Flow<AppResult<List<Diet>>> {
        val currentUserId = when (val userResult = authRepository.getCurrentSessionUser()) {
            is AppResult.Success -> userResult.data.id
            is AppResult.Error -> "prof_123"
        }

        return dietRepository.getDietsForProfessor(currentUserId)
    }
}
