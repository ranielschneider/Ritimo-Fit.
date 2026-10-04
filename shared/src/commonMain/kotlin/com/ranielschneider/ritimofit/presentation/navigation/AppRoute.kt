package com.ranielschneider.ritimofit.presentation.navigation

sealed interface AppRoute {
    data object Auth : AppRoute
    data class Professor(val route: ProfessorRoute = ProfessorRoute.Dashboard) : AppRoute
    data class Student(val route: StudentRoute = StudentRoute.Today()) : AppRoute
}
