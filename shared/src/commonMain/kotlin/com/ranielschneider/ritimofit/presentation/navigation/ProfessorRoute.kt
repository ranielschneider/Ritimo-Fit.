package com.ranielschneider.ritimofit.presentation.navigation

sealed interface ProfessorRoute {
    data object Dashboard : ProfessorRoute
    data class Students(val subRoute: StudentsSubRoute = StudentsSubRoute.List) : ProfessorRoute
    data class Planning(val subRoute: PlanningSubRoute = PlanningSubRoute.Workouts) : ProfessorRoute
    data object Activity : ProfessorRoute
    data class Profile(val subRoute: ProfileSubRoute = ProfileSubRoute.Main) : ProfessorRoute
}

sealed interface StudentsSubRoute {
    data object List : StudentsSubRoute
    data class Detail(
        val studentId: String,
        val tab: StudentDetailTab = StudentDetailTab.Overview
    ) : StudentsSubRoute
    data class AssignWorkoutPicker(val studentId: String) : StudentsSubRoute
}

enum class StudentDetailTab {
    Overview,
    Workout,
    Diet,
    Evolution,
    Exams,
    Documents,
    Reports
}

sealed interface PlanningSubRoute {
    data object Workouts : PlanningSubRoute
    data class WorkoutEditor(val workoutId: String? = null) : PlanningSubRoute
    data class ExercisePicker(val workoutId: String? = null) : PlanningSubRoute
    data object Diets : PlanningSubRoute
    data class Exercises(val selectedExerciseId: String? = null) : PlanningSubRoute
}

enum class ProfileSubRoute {
    Main,
    PersonalData,
    Settings,
    Theme,
    Notifications,
    Account
}
