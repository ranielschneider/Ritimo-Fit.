package com.ranielschneider.ritimofit.data.datasource

import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.DocumentCategory
import com.ranielschneider.ritimofit.domain.model.StudentDocument
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class InMemoryStudentDocumentDataSource : StudentDocumentDataSource {

    private val documentsState = MutableStateFlow<List<StudentDocument>>(
        listOf(
            StudentDocument(
                id = "doc_1",
                studentId = "std_1",
                title = "Hemograma Completo e Perfil Lipídico",
                category = DocumentCategory.CLINICAL_AND_HORMONAL_LABS,
                fileUri = "content://ritimofit/docs/hemograma.pdf",
                fileName = "hemograma_carlos_2024.pdf",
                fileExtension = "pdf",
                fileSizeBytes = 2_450_000L,
                examDateEpochMs = 1705000000000L,
                notes = "Valores de colesterol e glicemia de jejum dentro do padrão."
            ),
            StudentDocument(
                id = "doc_2",
                studentId = "std_1",
                title = "Ultrassom de Joelho Direito",
                category = DocumentCategory.IMAGING_EXAMS,
                fileUri = "content://ritimofit/docs/ultrassom_joelho.png",
                fileName = "ultrassom_joelho.png",
                fileExtension = "png",
                fileSizeBytes = 1_820_000L,
                examDateEpochMs = 1702500000000L,
                notes = "Avaliação de menisco e tendão patelar sem lesões."
            )
        )
    )

    override fun observeDocumentsForStudent(studentId: String): Flow<AppResult<List<StudentDocument>>> {
        return documentsState.map { list ->
            val filtered = list.filter { it.studentId == studentId }.sortedByDescending { it.createdAtEpochMs }
            AppResult.Success(filtered)
        }
    }

    override suspend fun addDocument(document: StudentDocument): AppResult<Unit> {
        val current = documentsState.value.toMutableList()
        current.add(document)
        documentsState.value = current
        return AppResult.Success(Unit)
    }

    override suspend fun deleteDocument(studentId: String, documentId: String): AppResult<Unit> {
        val current = documentsState.value.toMutableList()
        current.removeAll { it.studentId == studentId && it.id == documentId }
        documentsState.value = current
        return AppResult.Success(Unit)
    }
}
