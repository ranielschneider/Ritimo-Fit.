package com.ranielschneider.ritimofit.domain.model

data class ProfessorMessage(
    val id: String,
    val studentId: String,
    val professorName: String,
    val message: String,
    val sentAtEpochMs: Long = 0L
)
