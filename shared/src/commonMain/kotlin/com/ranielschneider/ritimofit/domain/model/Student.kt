package com.ranielschneider.ritimofit.domain.model

data class Student(
    val userId: String,
    val user: User,
    val professorId: String,
    val heightCm: Double? = null,
    val weightKg: Double? = null,
    val goals: String? = null
)
