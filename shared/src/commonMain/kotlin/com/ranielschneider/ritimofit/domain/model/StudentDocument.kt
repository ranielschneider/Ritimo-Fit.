package com.ranielschneider.ritimofit.domain.model

enum class DocumentCategory(val displayName: String) {
    CLINICAL_AND_HORMONAL_LABS("Análises clínicas e hormonais"),
    IMAGING_EXAMS("Exames de imagem"),
    PHYSICAL_ASSESSMENTS("Avaliações físicas"),
    OTHER("Outros documentos")
}

data class StudentDocument(
    val id: String,
    val studentId: String,
    val title: String,
    val category: DocumentCategory,
    val fileUri: String,
    val fileName: String,
    val fileExtension: String,
    val fileSizeBytes: Long,
    val examDateEpochMs: Long? = null,
    val notes: String? = null,
    val createdAtEpochMs: Long = 0L
)
