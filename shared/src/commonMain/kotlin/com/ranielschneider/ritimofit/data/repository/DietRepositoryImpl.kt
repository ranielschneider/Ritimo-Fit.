package com.ranielschneider.ritimofit.data.repository

import com.ranielschneider.ritimofit.core.dispatcher.AppDispatchers
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.data.datasource.DietDataSource
import com.ranielschneider.ritimofit.domain.model.Diet
import com.ranielschneider.ritimofit.domain.repository.DietRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class DietRepositoryImpl(
    private val dietDataSource: DietDataSource,
    private val dispatchers: AppDispatchers
) : DietRepository {

    override fun getDietsForProfessor(professorId: String): Flow<AppResult<List<Diet>>> {
        return dietDataSource.observeDietsForProfessor(professorId).flowOn(dispatchers.io)
    }

    override fun getDietForStudent(studentId: String): Flow<AppResult<Diet?>> {
        return flowOf(AppResult.Success(null)).flowOn(dispatchers.io)
    }

    override suspend fun getDietById(dietId: String): AppResult<Diet> {
        return withContext(dispatchers.io) {
            dietDataSource.getDietById(dietId)
        }
    }

    override suspend fun saveDiet(diet: Diet): AppResult<Unit> {
        return withContext(dispatchers.io) {
            dietDataSource.saveDiet(diet)
        }
    }

    override suspend fun deleteDiet(dietId: String): AppResult<Unit> {
        return withContext(dispatchers.io) {
            dietDataSource.deleteDiet(dietId)
        }
    }
}
