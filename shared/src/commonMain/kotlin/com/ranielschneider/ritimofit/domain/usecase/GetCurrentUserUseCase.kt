package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.User
import com.ranielschneider.ritimofit.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<User?> {
        return authRepository.getCurrentUser()
    }

    suspend fun getCurrentSessionUser(): AppResult<User> {
        return authRepository.getCurrentSessionUser()
    }
}
