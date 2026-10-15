package com.ranielschneider.ritimofit.domain.usecase

import com.ranielschneider.ritimofit.domain.calculator.NutritionalCalculator
import com.ranielschneider.ritimofit.domain.model.Diet
import com.ranielschneider.ritimofit.domain.model.FoodItem
import com.ranielschneider.ritimofit.domain.model.Meal
import com.ranielschneider.ritimofit.domain.model.MealCompletion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MealCompletionTest {

    private val meal1 = Meal(
        id = "m_1",
        name = "Café da manhã",
        foods = listOf(
            FoodItem(id = "f_1", name = "Ovo mexido", amount = "3", unit = "unidades", caloriesKcal = 219.0, proteinGrams = 19.95, carbsGrams = 0.9, fatGrams = 14.25),
            FoodItem(id = "f_2", name = "Pão integral", amount = "2", unit = "fatias", caloriesKcal = 126.5, proteinGrams = 4.7, carbsGrams = 24.95, fatGrams = 1.85)
        )
    )

    private val meal2 = Meal(
        id = "m_2",
        name = "Almoço",
        foods = listOf(
            FoodItem(id = "f_3", name = "Peito de frango", amount = "180", unit = "g", caloriesKcal = 286.2, proteinGrams = 57.6, carbsGrams = 0.0, fatGrams = 4.5),
            FoodItem(id = "f_4", name = "Arroz integral", amount = "200", unit = "g", caloriesKcal = 248.0, proteinGrams = 5.2, carbsGrams = 51.6, fatGrams = 2.0)
        )
    )

    private val legacyMealWithoutNutritionData = Meal(
        id = "m_3",
        name = "Lanche da tarde sem macros",
        foods = listOf(
            FoodItem(id = "f_legacy", name = "Maçã sem dados", amount = "1", unit = "unidade", caloriesKcal = 0.0, proteinGrams = 0.0, carbsGrams = 0.0, fatGrams = 0.0)
        )
    )

    private val diet = Diet(
        id = "dt_1",
        professorId = "prof_1",
        title = "Plano Teste",
        totalCalories = 2600,
        proteinGrams = 180,
        carbsGrams = 320,
        fatsGrams = 70,
        meals = listOf(meal1, meal2, legacyMealWithoutNutritionData)
    )

    @Test
    fun testMealCompletionAdditionAndDuplicatePrevention() {
        val completions = mutableListOf<MealCompletion>()

        val completion1 = MealCompletion(id = "c_1", studentId = "std_1", dietId = "dt_1", mealId = "m_1")

        // 1. Add first completion
        val existsBefore = completions.any { it.studentId == "std_1" && it.dietId == "dt_1" && it.mealId == "m_1" }
        assertFalse(existsBefore)
        if (!existsBefore) {
            completions.add(completion1)
        }
        assertEquals(1, completions.size)

        // 2. Try adding duplicate completion for same meal
        val existsAfter = completions.any { it.studentId == "std_1" && it.dietId == "dt_1" && it.mealId == "m_1" }
        assertTrue(existsAfter)
        if (!existsAfter) {
            completions.add(completion1)
        }
        assertEquals(1, completions.size)
    }

    @Test
    fun testMealCompletionUndo() {
        val completions = mutableListOf(
            MealCompletion(id = "c_1", studentId = "std_1", dietId = "dt_1", mealId = "m_1"),
            MealCompletion(id = "c_2", studentId = "std_1", dietId = "dt_1", mealId = "m_2")
        )

        assertEquals(2, completions.size)

        // Undo meal 1
        completions.removeAll { it.studentId == "std_1" && it.dietId == "dt_1" && it.mealId == "m_1" }

        assertEquals(1, completions.size)
        assertEquals("m_2", completions.first().mealId)
    }

    @Test
    fun testNutritionalConsumedSumOnlyForCompletedMeals() {
        val allMeals = listOf(meal1, meal2, legacyMealWithoutNutritionData)

        // Only meal1 is completed
        val completedMealIds = setOf("m_1")

        val completedMealsList = allMeals.filter { meal -> completedMealIds.contains(meal.id) }
        val consumedSummary = NutritionalCalculator.sumDietMacros(completedMealsList)

        // Only meal1 macros should be summed (345.5 kcal, 24.65g P, 25.85g C, 16.1g G)
        val expectedMeal1Summary = NutritionalCalculator.sumMealMacros(meal1)
        assertEquals(expectedMeal1Summary.caloriesKcal, consumedSummary.totalCaloriesKcal)
        assertEquals(expectedMeal1Summary.proteinGrams, consumedSummary.proteinGrams)
        assertEquals(expectedMeal1Summary.carbsGrams, consumedSummary.carbsGrams)
        assertEquals(expectedMeal1Summary.fatGrams, consumedSummary.fatGrams)

        // Verify target diet macros set by professor remain untouched (2600 kcal)
        assertEquals(2600, diet.totalCalories)
        assertEquals(180, diet.proteinGrams)
    }

    @Test
    fun testUndoingMealCompletionDeductsConsumedMacros() {
        val allMeals = listOf(meal1, meal2)

        // Both meal1 and meal2 completed
        val initialCompletedMealIds = mutableSetOf("m_1", "m_2")
        val initialCompletedList = allMeals.filter { meal -> initialCompletedMealIds.contains(meal.id) }
        val initialSummary = NutritionalCalculator.sumDietMacros(initialCompletedList)

        // Meal1 (345.5) + Meal2 (534.2) = 879.7 kcal
        assertEquals(879.7, initialSummary.totalCaloriesKcal)

        // Undo meal1
        initialCompletedMealIds.remove("m_1")
        val updatedCompletedList = allMeals.filter { meal -> initialCompletedMealIds.contains(meal.id) }
        val updatedSummary = NutritionalCalculator.sumDietMacros(updatedCompletedList)

        // Only meal2 remains (534.2 kcal)
        val expectedMeal2Summary = NutritionalCalculator.sumMealMacros(meal2)
        assertEquals(expectedMeal2Summary.caloriesKcal, updatedSummary.totalCaloriesKcal)
        assertEquals(expectedMeal2Summary.proteinGrams, updatedSummary.proteinGrams)
    }

    @Test
    fun testUncalculatedFoodsDoNotCorruptTotals() {
        val allMeals = listOf(meal1, legacyMealWithoutNutritionData)
        val completedMealIds = setOf("m_1", "m_3")

        val completedMealsList = allMeals.filter { meal -> completedMealIds.contains(meal.id) }
        val summary = NutritionalCalculator.sumDietMacros(completedMealsList)

        // Legacy meal without macros contributes 0.0 and doesn't crash or corrupt meal1 macros
        val meal1Summary = NutritionalCalculator.sumMealMacros(meal1)
        assertEquals(meal1Summary.caloriesKcal, summary.totalCaloriesKcal)
    }
}
