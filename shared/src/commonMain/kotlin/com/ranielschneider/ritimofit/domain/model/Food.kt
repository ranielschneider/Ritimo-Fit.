package com.ranielschneider.ritimofit.domain.model

data class Food(
    val id: String,
    val name: String,
    val source: String = "TACO / Referência Nacional",
    val caloriesPer100g: Double,
    val proteinPer100g: Double,
    val carbsPer100g: Double,
    val fatPer100g: Double,
    val referenceUnit: String = "g",
    val externalId: String? = null
)
