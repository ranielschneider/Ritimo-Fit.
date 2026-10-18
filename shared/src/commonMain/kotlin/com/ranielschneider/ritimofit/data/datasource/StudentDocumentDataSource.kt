package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.StudentDocument
import kotlinx.coroutines.flow.Flow

interface StudentDocumentDataSource {
    fun observeDocumentsForStudent(studentId: String): Flow<AppResult<List<StudentDocument>>>
    suspend fun addDocument(document: StudentDocument): AppResult<Unit>
    suspend fun deleteDocument(studentId: String, documentId: String): AppResult<Unit>
}
