package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppError
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.repository.AuthRepository

class ForgotPasswordUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): AppResult<Unit> {
        val trimmedEmail = email.trim()
        if (trimmedEmail.isBlank() || !trimmedEmail.contains("@") || !trimmedEmail.contains(".")) {
            return AppResult.Error(AppError.Auth.InvalidCredentials)
        }
        return authRepository.sendPasswordResetEmail(trimmedEmail)
    }
}
