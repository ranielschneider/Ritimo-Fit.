package com.ranielschneider.ritimofit.di

import com.ranielschneider.ritimofit.presentation.auth.AuthViewModel
import com.ranielschneider.ritimofit.presentation.navigation.MainNavigationViewModel
import com.ranielschneider.ritimofit.presentation.professor.ProfessorDashboardViewModel
import com.ranielschneider.ritimofit.presentation.viewmodel.DietViewModel
import com.ranielschneider.ritimofit.presentation.viewmodel.ExerciseViewModel
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentDetailViewModel
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentDietAssignmentViewModel
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentDietViewModel
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentDocumentViewModel
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentEvolutionViewModel
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentListViewModel
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentTodayViewModel
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentWorkoutAssignmentViewModel
import com.ranielschneider.ritimofit.presentation.viewmodel.StudentWorkoutsViewModel
import com.ranielschneider.ritimofit.presentation.viewmodel.WorkoutViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MainNavigationViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::ProfessorDashboardViewModel)
    viewModelOf(::StudentListViewModel)
    viewModelOf(::StudentDetailViewModel)
    viewModelOf(::ExerciseViewModel)
    viewModelOf(::WorkoutViewModel)
    viewModelOf(::StudentWorkoutAssignmentViewModel)
    viewModelOf(::StudentTodayViewModel)
    viewModelOf(::StudentWorkoutsViewModel)
    viewModelOf(::DietViewModel)
    viewModelOf(::StudentDietAssignmentViewModel)
    viewModelOf(::StudentDietViewModel)
    viewModelOf(::StudentEvolutionViewModel)
    viewModelOf(::StudentDocumentViewModel)
}
