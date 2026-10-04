package com.ranielschneider.ritimofit.data.repository

import com.ranielschneider.ritimofit.core.dispatcher.AppDispatchers
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Diet
import com.ranielschneider.ritimofit.domain.repository.DietRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class DietRepositoryImpl(
    private val dispatchers: AppDispatchers
) : DietRepository {

    private val diets = mutableListOf<Diet>()

    override fun getDietForStudent(studentId: String): Flow<AppResult<Diet?>> {
        val diet = diets.find { it.studentId == studentId }
        return flowOf(AppResult.Success(diet)).flowOn(dispatchers.io)
    }

    override suspend fun saveDiet(diet: Diet): AppResult<Unit> {
        return withContext(dispatchers.io) {
            diets.removeAll { it.studentId == diet.studentId }
            diets.add(diet)
            AppResult.Success(Unit)
        }
    }
}
