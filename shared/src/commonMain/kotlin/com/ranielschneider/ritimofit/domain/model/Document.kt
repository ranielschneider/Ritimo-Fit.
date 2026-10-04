package com.ranielschneider.ritimofit.domain.model

data class Document(
    val id: String,
    val studentId: String,
    val professorId: String,
    val title: String,
    val type: DocumentType,
    val fileUrl: String,
    val uploadedAtEpochMs: Long
)

enum class DocumentType {
    EXAM,
    MEDICAL_CERTIFICATE,
    OTHER
}
