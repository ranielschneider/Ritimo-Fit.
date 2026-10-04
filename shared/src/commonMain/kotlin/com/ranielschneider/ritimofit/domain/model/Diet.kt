package com.ranielschneider.ritimofit.domain.model

data class Diet(
    val id: String,
    val studentId: String,
    val professorId: String,
    val title: String,
    val meals: List<Meal> = emptyList()
)

data class Meal(
    val id: String,
    val name: String,
    val time: String,
    val description: String,
    val caloriesKcal: Int? = null
)
