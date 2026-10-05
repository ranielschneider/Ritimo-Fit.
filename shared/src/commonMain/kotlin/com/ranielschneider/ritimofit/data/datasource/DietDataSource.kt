package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Diet
import kotlinx.coroutines.flow.Flow

interface DietDataSource {
    fun observeDietsForProfessor(professorId: String): Flow<AppResult<List<Diet>>>
    suspend fun getDietById(dietId: String): AppResult<Diet>
    suspend fun saveDiet(diet: Diet): AppResult<Unit>
    suspend fun deleteDiet(dietId: String): AppResult<Unit>
}
