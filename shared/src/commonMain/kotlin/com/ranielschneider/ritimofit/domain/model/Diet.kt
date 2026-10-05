package com.ranielschneider.ritimofit.domain.model

data class Diet(
    val id: String,
    val professorId: String,
    val title: String,
    val description: String? = null,
    val studentId: String? = null,
    val totalCalories: Int? = null,
    val proteinGrams: Int? = null,
    val carbsGrams: Int? = null,
    val fatsGrams: Int? = null,
    val meals: List<Meal> = emptyList(),
    val createdAtEpochMs: Long = 0L
)

data class Meal(
    val id: String,
    val name: String,
    val time: String? = null,
    val foods: List<FoodItem> = emptyList(),
    val notes: String? = null,
    val order: Int = 0
)

data class FoodItem(
    val id: String,
    val name: String,
    val amount: String,
    val unit: String,
    val notes: String? = null
)
