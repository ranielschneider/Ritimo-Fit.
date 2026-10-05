package com.ranielschneider.ritimofit.domain.repository

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.Diet
import kotlinx.coroutines.flow.Flow

interface DietRepository {
    fun getDietsForProfessor(professorId: String): Flow<AppResult<List<Diet>>>
    fun getDietForStudent(studentId: String): Flow<AppResult<Diet?>>
    suspend fun getDietById(dietId: String): AppResult<Diet>
    suspend fun saveDiet(diet: Diet): AppResult<Unit>
    suspend fun deleteDiet(dietId: String): AppResult<Unit>
}
