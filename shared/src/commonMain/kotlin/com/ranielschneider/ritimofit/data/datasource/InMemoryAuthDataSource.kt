package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppError
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.User
import com.ranielschneider.ritimofit.domain.model.UserRole
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryAuthDataSource : AuthDataSource {
    private val currentUserState = MutableStateFlow<User?>(null)

    // Fixed development accounts map
    private val registeredUsersMap = mutableMapOf<String, User>(
        "professor@ritimofit.dev" to User(
            id = "prof_123",
            email = "professor@ritimofit.dev",
            fullName = "Professor Ritimo",
            role = UserRole.PROFESSOR
        ),
        "professor@ritimofit.com" to User(
            id = "prof_123",
            email = "professor@ritimofit.com",
            fullName = "Professor Ritimo",
            role = UserRole.PROFESSOR
        ),
        "aluno@ritimofit.dev" to User(
            id = "std_1",
            email = "aluno@ritimofit.dev",
            fullName = "Carlos Aluno",
            role = UserRole.STUDENT
        ),
        "aluno1@ritimofit.com" to User(
            id = "std_1",
            email = "aluno1@ritimofit.com",
            fullName = "Carlos Aluno",
            role = UserRole.STUDENT
        ),
        "aluno2@ritimofit.com" to User(
            id = "std_2",
            email = "aluno2@ritimofit.com",
            fullName = "Mariana Aluna",
            role = UserRole.STUDENT
        )
    )

    override fun observeCurrentUser(): Flow<User?> = currentUserState.asStateFlow()

    override suspend fun getCurrentUser(): AppResult<User> {
        val user = currentUserState.value
        return if (user != null) {
            AppResult.Success(user)
        } else {
            AppResult.Error(AppError.Auth.Unauthenticated)
        }
    }

    override suspend fun login(email: String, password: String): AppResult<User> {
        delay(400) // Simulate short network latency
        val key = email.trim().lowercase()

        if (key.contains("error")) {
            return AppResult.Error(AppError.Auth.Unauthorized("E-mail ou senha incorretos."))
        }

        val existingUser = registeredUsersMap[key]
        val user = if (existingUser != null) {
            existingUser
        } else {
            val role = if (key.contains("aluno") || key.contains("student")) {
                UserRole.STUDENT
            } else {
                UserRole.PROFESSOR
            }
            val newUser = User(
                id = if (role == UserRole.PROFESSOR) "prof_123" else "std_${key.hashCode()}",
                email = email,
                fullName = if (role == UserRole.PROFESSOR) "Professor Ritimo" else "Aluno Ritimo",
                role = role
            )
            registeredUsersMap[key] = newUser
            newUser
        }

        currentUserState.value = user
        return AppResult.Success(user)
    }

    override suspend fun sendPasswordResetEmail(email: String): AppResult<Unit> {
        delay(400)
        if (email.contains("error", ignoreCase = true)) {
            return AppResult.Error(AppError.NotFound("E-mail não cadastrado no sistema."))
        }
        return AppResult.Success(Unit)
    }

    override suspend fun register(
        fullName: String,
        email: String,
        password: String,
        role: UserRole
    ): AppResult<User> {
        delay(400)
        val key = email.trim().lowercase()
        val user = User(
            id = if (role == UserRole.PROFESSOR) "prof_123" else "std_${key.hashCode()}",
            email = email,
            fullName = fullName,
            role = role
        )
        registeredUsersMap[key] = user
        currentUserState.value = user
        return AppResult.Success(user)
    }

    override suspend fun logout(): AppResult<Unit> {
        currentUserState.value = null
        return AppResult.Success(Unit)
    }
}
