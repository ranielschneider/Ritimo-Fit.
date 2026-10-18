package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.StudentDocument
import com.ranielschneider.ritimofit.domain.repository.StudentDocumentRepository
import kotlinx.coroutines.flow.Flow

class GetStudentDocumentsUseCase(
    private val studentDocumentRepository: StudentDocumentRepository
) {
    operator fun invoke(studentId: String): Flow<AppResult<List<StudentDocument>>> {
        return studentDocumentRepository.getDocumentsForStudent(studentId)
    }
}
