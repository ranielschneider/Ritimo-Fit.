package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.User
import com.ranielschneider.ritimofit.domain.model.UserRole
import kotlinx.coroutines.flow.Flow

interface AuthDataSource {
    fun observeCurrentUser(): Flow<User?>
    suspend fun getCurrentUser(): AppResult<User>
    suspend fun login(email: String, password: String): AppResult<User>
    suspend fun sendPasswordResetEmail(email: String): AppResult<Unit>
    suspend fun register(fullName: String, email: String, password: String, role: UserRole): AppResult<User>
    suspend fun logout(): AppResult<Unit>
}
