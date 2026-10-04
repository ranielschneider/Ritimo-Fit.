package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppError
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.User
import com.ranielschneider.ritimofit.domain.model.UserRole
import com.ranielschneider.ritimofit.domain.repository.AuthRepository

class RegisterUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        fullName: String,
        email: String,
        password: String,
        role: UserRole
    ): AppResult<User> {
        val trimmedName = fullName.trim()
        val trimmedEmail = email.trim()

        if (trimmedName.length < 2) {
            return AppResult.Error(AppError.Auth.Unauthorized("Informe seu nome completo"))
        }
        if (trimmedEmail.isBlank() || !trimmedEmail.contains("@") || !trimmedEmail.contains(".")) {
            return AppResult.Error(AppError.Auth.InvalidCredentials)
        }
        if (password.length < 6) {
            return AppResult.Error(AppError.Auth.Unauthorized("A senha deve conter no mínimo 6 caracteres"))
        }

        return authRepository.register(trimmedName, trimmedEmail, password, role)
    }
}
