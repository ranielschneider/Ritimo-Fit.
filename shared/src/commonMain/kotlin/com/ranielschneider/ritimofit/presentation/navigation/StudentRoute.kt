package com.ranielschneider.ritimofit.presentation.navigation

sealed interface StudentRoute {
    data class Today(val viewingWorkoutId: String? = null) : StudentRoute
    data class Workouts(val subRoute: WorkoutsSubRoute = WorkoutsSubRoute.TodayWorkout()) : StudentRoute
    data class Diet(val subRoute: DietSubRoute = DietSubRoute.TodayDiet) : StudentRoute
    data object Evolution : StudentRoute
    data class Profile(val subRoute: ProfileSubRoute = ProfileSubRoute.Main) : StudentRoute
}

sealed interface WorkoutsSubRoute {
    data class TodayWorkout(val viewingWorkoutId: String? = null) : WorkoutsSubRoute
    data object UpcomingWorkouts : WorkoutsSubRoute
    data object History : WorkoutsSubRoute
}

sealed interface DietSubRoute {
    data object TodayDiet : DietSubRoute
    data object History : DietSubRoute
}
