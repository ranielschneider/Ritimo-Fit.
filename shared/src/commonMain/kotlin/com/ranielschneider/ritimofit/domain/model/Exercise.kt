package com.ranielschneider.ritimofit.domain.model

data class Exercise(
    val id: String,
    val name: String,
    val muscleGroup: String,
    val equipment: String? = null,
    val description: String? = null,
    val instructions: List<String> = emptyList(),
    val gifUrl: String? = null,
    val videoUrl: String? = null,
    val isCustom: Boolean = false,
    val createdByProfessorId: String? = null
)
