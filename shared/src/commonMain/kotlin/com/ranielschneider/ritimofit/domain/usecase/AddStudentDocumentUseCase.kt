package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.core.result.AppError
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.DocumentCategory
import com.ranielschneider.ritimofit.domain.model.StudentDocument
import com.ranielschneider.ritimofit.domain.repository.StudentDocumentRepository

class AddStudentDocumentUseCase(
    private val studentDocumentRepository: StudentDocumentRepository
) {
    companion object {
        val ALLOWED_EXTENSIONS = setOf("pdf", "jpg", "jpeg", "png")
        const val MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024L // 10 MB limit
    }

    suspend operator fun invoke(
        studentId: String,
        title: String,
        category: DocumentCategory,
        fileUri: String,
        fileName: String,
        fileExtension: String,
        fileSizeBytes: Long,
        examDateEpochMs: Long? = null,
        notes: String? = null
    ): AppResult<Unit> {
        val trimmedTitle = title.trim()
        if (trimmedTitle.isEmpty()) {
            return AppResult.Error(AppError.PermissionDenied("O título do documento é obrigatório."))
        }

        val ext = fileExtension.lowercase().trim()
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            return AppResult.Error(
                AppError.PermissionDenied("Formato de arquivo '.$ext' não suportado. Envie apenas arquivos PDF, JPG, JPEG ou PNG.")
            )
        }

        if (fileSizeBytes <= 0 || fileSizeBytes > MAX_FILE_SIZE_BYTES) {
            return AppResult.Error(
                AppError.PermissionDenied("O tamanho do arquivo excede o limite permitido de 10 MB.")
            )
        }

        val document = StudentDocument(
            id = "doc_" + (10000..99999).random(),
            studentId = studentId,
            title = trimmedTitle,
            category = category,
            fileUri = fileUri,
            fileName = fileName,
            fileExtension = ext,
            fileSizeBytes = fileSizeBytes,
            examDateEpochMs = examDateEpochMs,
            notes = notes?.trim()?.takeIf { it.isNotEmpty() },
            createdAtEpochMs = 0L
        )

        return studentDocumentRepository.addDocument(document)
    }
}
