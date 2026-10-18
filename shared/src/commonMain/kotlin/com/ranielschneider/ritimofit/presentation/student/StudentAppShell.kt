package com.ranielschneider.ritimofit.presentation.student

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.presentation.navigation.MainNavigationViewModel
import com.ranielschneider.ritimofit.presentation.navigation.NavigationTabItem
import com.ranielschneider.ritimofit.presentation.navigation.RitimoBottomNavigation
import com.ranielschneider.ritimofit.presentation.navigation.StudentRoute
import com.ranielschneider.ritimofit.presentation.navigation.WorkoutsSubRoute

@Composable
fun StudentAppShell(
    currentRoute: StudentRoute,
    viewModel: MainNavigationViewModel,
    modifier: Modifier = Modifier
) {
    val navItems = listOf(
        NavigationTabItem(
            label = "Hoje",
            icon = RitimoIcons.Today,
            isSelected = currentRoute is StudentRoute.Today,
            onClick = { viewModel.navigateToStudentRoute(StudentRoute.Today()) }
        ),
        NavigationTabItem(
            label = "Treinos",
            icon = RitimoIcons.Workouts,
            isSelected = currentRoute is StudentRoute.Workouts,
            onClick = { viewModel.navigateToStudentRoute(StudentRoute.Workouts()) }
        ),
        NavigationTabItem(
            label = "Dieta",
            icon = RitimoIcons.Diet,
            isSelected = currentRoute is StudentRoute.Diet,
            onClick = { viewModel.navigateToStudentRoute(StudentRoute.Diet()) }
        ),
        NavigationTabItem(
            label = "Evolução",
            icon = RitimoIcons.Evolution,
            isSelected = currentRoute is StudentRoute.Evolution,
            onClick = { viewModel.navigateToStudentRoute(StudentRoute.Evolution) }
        ),
        NavigationTabItem(
            label = "Perfil",
            icon = RitimoIcons.User,
            isSelected = currentRoute is StudentRoute.Profile,
            onClick = { viewModel.navigateToStudentRoute(StudentRoute.Profile()) }
        )
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            RitimoBottomNavigation(items = navItems)
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentRoute) {
                is StudentRoute.Today -> {
                    if (currentRoute.viewingWorkoutId == null) {
                        StudentTodayScreen(
                            onViewWorkout = { workoutId ->
                                viewModel.navigateToStudentRoute(
                                    StudentRoute.Today(viewingWorkoutId = workoutId)
                                )
                            }
                        )
                    } else {
                        StudentWorkoutDetailScreen(
                            workoutId = currentRoute.viewingWorkoutId,
                            onBack = {
                                viewModel.navigateToStudentRoute(
                                    StudentRoute.Today(viewingWorkoutId = null)
                                )
                            }
                        )
                    }
                }
                is StudentRoute.Workouts -> {
                    when (val subRoute = currentRoute.subRoute) {
                        is WorkoutsSubRoute.TodayWorkout -> {
                            if (subRoute.viewingWorkoutId == null) {
                                StudentWorkoutsScreen(
                                    onViewWorkoutDetail = { workoutId ->
                                        viewModel.navigateToStudentRoute(
                                            StudentRoute.Workouts(WorkoutsSubRoute.TodayWorkout(viewingWorkoutId = workoutId))
                                        )
                                    }
                                )
                            } else {
                                StudentWorkoutDetailScreen(
                                    workoutId = subRoute.viewingWorkoutId,
                                    onBack = {
                                        viewModel.navigateToStudentRoute(
                                            StudentRoute.Workouts(WorkoutsSubRoute.TodayWorkout(viewingWorkoutId = null))
                                        )
                                    }
                                )
                            }
                        }
                        is WorkoutsSubRoute.UpcomingWorkouts -> {
                            StudentWorkoutsScreen(
                                onViewWorkoutDetail = {}
                            )
                        }
                        is WorkoutsSubRoute.History -> {
                            StudentWorkoutsScreen(
                                onViewWorkoutDetail = {}
                            )
                        }
                    }
                }
                is StudentRoute.Diet -> {
                    StudentDietScreen()
                }
                is StudentRoute.Evolution -> {
                    StudentEvolutionScreen()
                }
                is StudentRoute.Profile -> {
                    StudentProfileScreen(
                        onLogout = { viewModel.logout() }
                    )
                }
            }
        }
    }
}
