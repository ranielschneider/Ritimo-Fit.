package com.ranielschneider.ritimofit.domain.calculator

import com.ranielschneider.ritimofit.domain.model.Food
import com.ranielschneider.ritimofit.domain.model.FoodItem
import com.ranielschneider.ritimofit.domain.model.Meal
import kotlin.math.roundToInt

data class MealNutritionalSummary(
    val caloriesKcal: Double,
    val proteinGrams: Double,
    val carbsGrams: Double,
    val fatGrams: Double
)

data class DietNutritionalSummary(
    val totalCaloriesKcal: Double,
    val proteinGrams: Double,
    val carbsGrams: Double,
    val fatGrams: Double
)

object NutritionalCalculator {

    fun calculatePortion(
        food: Food,
        amountValue: Double,
        unit: String,
        gramsPerUnit: Double = 100.0,
        notes: String? = null
    ): FoodItem {
        val totalGrams = when (unit.trim().lowercase()) {
            "g", "ml" -> amountValue
            "unidade", "unidades" -> amountValue * gramsPerUnit
            "colher", "colheres", "colher de sopa" -> amountValue * 15.0
            "porção", "porções" -> amountValue * gramsPerUnit
            else -> amountValue
        }

        val factor = (totalGrams / 100.0).coerceAtLeast(0.0)
        val calories = (food.caloriesPer100g * factor)
        val protein = (food.proteinPer100g * factor)
        val carbs = (food.carbsPer100g * factor)
        val fat = (food.fatPer100g * factor)

        val formattedAmount = if (amountValue % 1.0 == 0.0) {
            amountValue.toInt().toString()
        } else {
            amountValue.toString()
        }

        return FoodItem(
            id = "f_" + (10000..99999).random(),
            foodId = food.id,
            name = food.name,
            amount = formattedAmount,
            unit = unit.trim(),
            caloriesKcal = roundToTwoDecimals(calories),
            proteinGrams = roundToTwoDecimals(protein),
            carbsGrams = roundToTwoDecimals(carbs),
            fatGrams = roundToTwoDecimals(fat),
            isCustom = food.source == "Personalizado",
            notes = notes
        )
    }

    fun sumMealMacros(meal: Meal): MealNutritionalSummary {
        var kcal = 0.0
        var p = 0.0
        var c = 0.0
        var f = 0.0
        meal.foods.forEach { food ->
            kcal += food.caloriesKcal
            p += food.proteinGrams
            c += food.carbsGrams
            f += food.fatGrams
        }
        return MealNutritionalSummary(
            caloriesKcal = roundToTwoDecimals(kcal),
            proteinGrams = roundToTwoDecimals(p),
            carbsGrams = roundToTwoDecimals(c),
            fatGrams = roundToTwoDecimals(f)
        )
    }

    fun sumDietMacros(meals: List<Meal>): DietNutritionalSummary {
        var kcal = 0.0
        var p = 0.0
        var c = 0.0
        var f = 0.0
        meals.forEach { meal ->
            val summary = sumMealMacros(meal)
            kcal += summary.caloriesKcal
            p += summary.proteinGrams
            c += summary.carbsGrams
            f += summary.fatGrams
        }
        return DietNutritionalSummary(
            totalCaloriesKcal = roundToTwoDecimals(kcal),
            proteinGrams = roundToTwoDecimals(p),
            carbsGrams = roundToTwoDecimals(c),
            fatGrams = roundToTwoDecimals(f)
        )
    }

    private fun roundToTwoDecimals(value: Double): Double {
        return (value * 100.0).roundToInt() / 100.0
    }
}
