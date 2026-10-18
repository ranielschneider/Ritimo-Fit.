package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.repository.StudentDocumentRepository

class DeleteStudentDocumentUseCase(
    private val studentDocumentRepository: StudentDocumentRepository
) {
    suspend operator fun invoke(studentId: String, documentId: String): AppResult<Unit> {
        return studentDocumentRepository.deleteDocument(studentId, documentId)
    }
}
