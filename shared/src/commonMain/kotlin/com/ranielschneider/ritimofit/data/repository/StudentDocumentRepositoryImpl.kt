package com.ranielschneider.ritimofit.data.repository

import com.ranielschneider.ritimofit.core.dispatcher.AppDispatchers
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.data.datasource.StudentDocumentDataSource
import com.ranielschneider.ritimofit.domain.model.StudentDocument
import com.ranielschneider.ritimofit.domain.repository.StudentDocumentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class StudentDocumentRepositoryImpl(
    private val studentDocumentDataSource: StudentDocumentDataSource,
    private val dispatchers: AppDispatchers
) : StudentDocumentRepository {

    override fun getDocumentsForStudent(studentId: String): Flow<AppResult<List<StudentDocument>>> {
        return studentDocumentDataSource.observeDocumentsForStudent(studentId).flowOn(dispatchers.io)
    }

    override suspend fun addDocument(document: StudentDocument): AppResult<Unit> {
        return withContext(dispatchers.io) {
            studentDocumentDataSource.addDocument(document)
        }
    }

    override suspend fun deleteDocument(studentId: String, documentId: String): AppResult<Unit> {
        return withContext(dispatchers.io) {
            studentDocumentDataSource.deleteDocument(studentId, documentId)
        }
    }
}
