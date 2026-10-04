package com.ranielschneider.ritimofit.domain.repository

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.User
import com.ranielschneider.ritimofit.domain.model.UserRole
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUser(): Flow<User?>
    suspend fun getCurrentSessionUser(): AppResult<User>
    suspend fun login(email: String, password: String): AppResult<User>
    suspend fun sendPasswordResetEmail(email: String): AppResult<Unit>
    suspend fun register(fullName: String, email: String, password: String, role: UserRole): AppResult<User>
    suspend fun logout(): AppResult<Unit>
}
