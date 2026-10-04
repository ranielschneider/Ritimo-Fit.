package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppError
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.User
import com.ranielschneider.ritimofit.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): AppResult<User> {
        val trimmedEmail = email.trim()
        if (trimmedEmail.isBlank() || !trimmedEmail.contains("@") || !trimmedEmail.contains(".")) {
            return AppResult.Error(AppError.Auth.InvalidCredentials)
        }
        if (password.length < 6) {
            return AppResult.Error(AppError.Auth.InvalidCredentials)
        }
        return authRepository.login(trimmedEmail, password)
    }
}
