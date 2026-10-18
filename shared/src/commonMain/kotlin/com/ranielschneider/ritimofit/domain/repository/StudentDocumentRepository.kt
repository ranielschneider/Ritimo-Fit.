package com.ranielschneider.ritimofit.domain.repository

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.StudentDocument
import kotlinx.coroutines.flow.Flow

interface StudentDocumentRepository {
    fun getDocumentsForStudent(studentId: String): Flow<AppResult<List<StudentDocument>>>
    suspend fun addDocument(document: StudentDocument): AppResult<Unit>
    suspend fun deleteDocument(studentId: String, documentId: String): AppResult<Unit>
}
