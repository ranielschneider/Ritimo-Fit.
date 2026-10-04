package com.ranielschneider.ritimofit.data.repository

import com.ranielschneider.ritimofit.core.dispatcher.AppDispatchers
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.data.datasource.AuthDataSource
import com.ranielschneider.ritimofit.domain.model.User
import com.ranielschneider.ritimofit.domain.model.UserRole
import com.ranielschneider.ritimofit.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource,
    private val dispatchers: AppDispatchers
) : AuthRepository {

    override fun getCurrentUser(): Flow<User?> {
        return authDataSource.observeCurrentUser().flowOn(dispatchers.io)
    }

    override suspend fun getCurrentSessionUser(): AppResult<User> {
        return withContext(dispatchers.io) {
            authDataSource.getCurrentUser()
        }
    }

    override suspend fun login(email: String, password: String): AppResult<User> {
        return withContext(dispatchers.io) {
            authDataSource.login(email, password)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): AppResult<Unit> {
        return withContext(dispatchers.io) {
            authDataSource.sendPasswordResetEmail(email)
        }
    }

    override suspend fun register(
        fullName: String,
        email: String,
        password: String,
        role: UserRole
    ): AppResult<User> {
        return withContext(dispatchers.io) {
            authDataSource.register(fullName, email, password, role)
        }
    }

    override suspend fun logout(): AppResult<Unit> {
        return withContext(dispatchers.io) {
            authDataSource.logout()
        }
    }
}
