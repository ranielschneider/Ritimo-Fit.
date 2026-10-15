package com.ranielschneider.ritimofit.domain.calculator

import com.ranielschneider.ritimofit.domain.model.Food
import com.ranielschneider.ritimofit.domain.model.Meal
import kotlin.test.Test
import kotlin.test.assertEquals

class NutritionalCalculatorTest {

    private val arroz = Food(
        id = "fd_arroz",
        name = "Arroz branco cozido",
        caloriesPer100g = 128.0,
        proteinPer100g = 2.7,
        carbsPer100g = 28.0,
        fatPer100g = 0.3
    )

    private val frango = Food(
        id = "fd_frango",
        name = "Peito de frango grelhado",
        caloriesPer100g = 159.0,
        proteinPer100g = 32.0,
        carbsPer100g = 0.0,
        fatPer100g = 2.5
    )

    private val ovo = Food(
        id = "fd_ovo",
        name = "Ovo de galinha cozido",
        caloriesPer100g = 146.0,
        proteinPer100g = 13.3,
        carbsPer100g = 0.6,
        fatPer100g = 9.5
    )

    @Test
    fun testProportionalPortionCalculationForGrams() {
        val portion = NutritionalCalculator.calculatePortion(
            food = arroz,
            amountValue = 150.0,
            unit = "g"
        )

        assertEquals("Arroz branco cozido", portion.name)
        assertEquals("150", portion.amount)
        assertEquals("g", portion.unit)
        assertEquals(192.0, portion.caloriesKcal)
        assertEquals(4.05, portion.proteinGrams)
        assertEquals(42.0, portion.carbsGrams)
        assertEquals(0.45, portion.fatGrams)
    }

    @Test
    fun testUnitConversionForEggs() {
        // 2 unidades of egg with 50g per egg = 100g total
        val portion = NutritionalCalculator.calculatePortion(
            food = ovo,
            amountValue = 2.0,
            unit = "unidade",
            gramsPerUnit = 50.0
        )

        assertEquals("2", portion.amount)
        assertEquals("unidade", portion.unit)
        assertEquals(146.0, portion.caloriesKcal)
        assertEquals(13.3, portion.proteinGrams)
        assertEquals(0.6, portion.carbsGrams)
        assertEquals(9.5, portion.fatGrams)
    }

    @Test
    fun testSumMealAndDietMacros() {
        val portionArroz = NutritionalCalculator.calculatePortion(arroz, 200.0, "g") // 256 kcal, 5.4g P, 56g C, 0.6g G
        val portionFrango = NutritionalCalculator.calculatePortion(frango, 150.0, "g") // 238.5 kcal, 48g P, 0g C, 3.75g G

        val meal = Meal(
            id = "m_1",
            name = "Almoço",
            foods = listOf(portionArroz, portionFrango)
        )

        val mealSummary = NutritionalCalculator.sumMealMacros(meal)
        assertEquals(494.5, mealSummary.caloriesKcal)
        assertEquals(53.4, mealSummary.proteinGrams)
        assertEquals(56.0, mealSummary.carbsGrams)
        assertEquals(4.35, mealSummary.fatGrams)

        val dietSummary = NutritionalCalculator.sumDietMacros(listOf(meal))
        assertEquals(494.5, dietSummary.totalCaloriesKcal)
        assertEquals(53.4, dietSummary.proteinGrams)
        assertEquals(56.0, dietSummary.carbsGrams)
        assertEquals(4.35, dietSummary.fatGrams)
    }
}
