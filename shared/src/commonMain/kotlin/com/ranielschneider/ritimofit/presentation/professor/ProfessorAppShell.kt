package com.ranielschneider.ritimofit.presentation.professor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ranielschneider.ritimofit.core.design.icons.RitimoIcons
import com.ranielschneider.ritimofit.domain.model.Exercise
import com.ranielschneider.ritimofit.presentation.base.UiState
import com.ranielschneider.ritimofit.presentation.navigation.MainNavigationViewModel
import com.ranielschneider.ritimofit.presentation.navigation.NavigationTabItem
import com.ranielschneider.ritimofit.presentation.navigation.PlanningSubRoute
import com.ranielschneider.ritimofit.presentation.navigation.ProfessorRoute
import com.ranielschneider.ritimofit.presentation.navigation.RitimoBottomNavigation
import com.ranielschneider.ritimofit.presentation.navigation.RoutePlaceholderScreen
import com.ranielschneider.ritimofit.presentation.navigation.StudentsSubRoute
import com.ranielschneider.ritimofit.presentation.viewmodel.ExerciseViewModel
import com.ranielschneider.ritimofit.presentation.viewmodel.WorkoutViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfessorAppShell(
    currentRoute: ProfessorRoute,
    viewModel: MainNavigationViewModel,
    modifier: Modifier = Modifier
) {
    val navItems = listOf(
        NavigationTabItem(
            label = "Dashboard",
            icon = RitimoIcons.Dashboard,
            isSelected = currentRoute is ProfessorRoute.Dashboard,
            onClick = { viewModel.navigateToProfessorRoute(ProfessorRoute.Dashboard) }
        ),
        NavigationTabItem(
            label = "Alunos",
            icon = RitimoIcons.Students,
            isSelected = currentRoute is ProfessorRoute.Students,
            onClick = { viewModel.navigateToProfessorRoute(ProfessorRoute.Students()) }
        ),
        NavigationTabItem(
            label = "Planejar",
            icon = RitimoIcons.Planning,
            isSelected = currentRoute is ProfessorRoute.Planning,
            onClick = { viewModel.navigateToProfessorRoute(ProfessorRoute.Planning()) }
        ),
        NavigationTabItem(
            label = "Atividade",
            icon = RitimoIcons.Activity,
            isSelected = currentRoute is ProfessorRoute.Activity,
            onClick = { viewModel.navigateToProfessorRoute(ProfessorRoute.Activity) }
        ),
        NavigationTabItem(
            label = "Perfil",
            icon = RitimoIcons.User,
            isSelected = currentRoute is ProfessorRoute.Profile,
            onClick = { viewModel.navigateToProfessorRoute(ProfessorRoute.Profile()) }
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
                is ProfessorRoute.Dashboard -> {
                    ProfessorDashboardScreen(
                        onNavigateToRoute = { route -> viewModel.navigateToProfessorRoute(route) }
                    )
                }
                is ProfessorRoute.Students -> {
                    when (val subRoute = currentRoute.subRoute) {
                        is StudentsSubRoute.List -> {
                            StudentListScreen(
                                onSelectStudent = { studentId ->
                                    viewModel.navigateToProfessorRoute(
                                        ProfessorRoute.Students(StudentsSubRoute.Detail(studentId = studentId))
                                    )
                                }
                            )
                        }
                        is StudentsSubRoute.Detail -> {
                            StudentDetailScreen(
                                studentId = subRoute.studentId,
                                onBack = {
                                    viewModel.navigateToProfessorRoute(
                                        ProfessorRoute.Students(StudentsSubRoute.List)
                                    )
                                },
                                onNavigateToAssignWorkoutPicker = {
                                    viewModel.navigateToProfessorRoute(
                                        ProfessorRoute.Students(StudentsSubRoute.AssignWorkoutPicker(studentId = subRoute.studentId))
                                    )
                                }
                            )
                        }
                        is StudentsSubRoute.AssignWorkoutPicker -> {
                            AssignWorkoutPickerScreen(
                                studentId = subRoute.studentId,
                                onBack = {
                                    viewModel.navigateToProfessorRoute(
                                        ProfessorRoute.Students(StudentsSubRoute.Detail(studentId = subRoute.studentId))
                                    )
                                }
                            )
                        }
                    }
                }
                is ProfessorRoute.Planning -> {
                    val workoutVM: WorkoutViewModel = koinViewModel()

                    when (val subRoute = currentRoute.subRoute) {
                        is PlanningSubRoute.Workouts -> {
                            WorkoutListScreen(
                                onCreateWorkout = {
                                    workoutVM.initNewWorkout()
                                    viewModel.navigateToProfessorRoute(
                                        ProfessorRoute.Planning(PlanningSubRoute.WorkoutEditor())
                                    )
                                },
                                onEditWorkout = { workoutId ->
                                    workoutVM.loadWorkoutForEditing(workoutId)
                                    viewModel.navigateToProfessorRoute(
                                        ProfessorRoute.Planning(PlanningSubRoute.WorkoutEditor(workoutId = workoutId))
                                    )
                                },
                                onTabSelected = { tab ->
                                    when (tab) {
                                        "Treinos" -> viewModel.navigateToProfessorRoute(ProfessorRoute.Planning(PlanningSubRoute.Workouts))
                                        "Dietas" -> viewModel.navigateToProfessorRoute(ProfessorRoute.Planning(PlanningSubRoute.Diets))
                                        "Exercícios" -> viewModel.navigateToProfessorRoute(ProfessorRoute.Planning(PlanningSubRoute.Exercises()))
                                    }
                                },
                                viewModel = workoutVM
                            )
                        }
                        is PlanningSubRoute.WorkoutEditor -> {
                            WorkoutEditorScreen(
                                workoutIdToEdit = subRoute.workoutId,
                                onBack = {
                                    viewModel.navigateToProfessorRoute(
                                        ProfessorRoute.Planning(PlanningSubRoute.Workouts)
                                    )
                                },
                                onAddExerciseFromCatalog = {
                                    viewModel.navigateToProfessorRoute(
                                        ProfessorRoute.Planning(PlanningSubRoute.ExercisePicker(workoutId = subRoute.workoutId))
                                    )
                                },
                                viewModel = workoutVM
                            )
                        }
                        is PlanningSubRoute.ExercisePicker -> {
                            val exerciseVM: ExerciseViewModel = koinViewModel()
                            val exerciseDetailState by exerciseVM.exerciseDetailState.collectAsState()

                            LaunchedEffect(Unit) {
                                exerciseVM.clearExerciseDetailState()
                            }

                            ExerciseListScreen(
                                onSelectExercise = { exerciseId ->
                                    exerciseVM.loadExerciseDetail(exerciseId)
                                },
                                viewModel = exerciseVM
                            )

                            LaunchedEffect(exerciseDetailState) {
                                if (exerciseDetailState is UiState.Success) {
                                    val exercise = (exerciseDetailState as UiState.Success<Exercise>).data
                                    workoutVM.addExerciseFromCatalog(exercise)
                                    exerciseVM.clearExerciseDetailState()
                                    viewModel.navigateToProfessorRoute(
                                        ProfessorRoute.Planning(PlanningSubRoute.WorkoutEditor(workoutId = subRoute.workoutId))
                                    )
                                }
                            }
                        }
                        is PlanningSubRoute.Diets -> {
                            RoutePlaceholderScreen(
                                badgeText = "ÁREA DO PROFESSOR",
                                title = "Prescrição de Dietas",
                                description = "Elabore e monte planos alimentares para seus alunos.",
                                subTabs = listOf("Treinos", "Dietas", "Exercícios"),
                                activeTab = "Dietas",
                                onTabSelected = { tab ->
                                    when (tab) {
                                        "Treinos" -> viewModel.navigateToProfessorRoute(ProfessorRoute.Planning(PlanningSubRoute.Workouts))
                                        "Dietas" -> viewModel.navigateToProfessorRoute(ProfessorRoute.Planning(PlanningSubRoute.Diets))
                                        "Exercícios" -> viewModel.navigateToProfessorRoute(ProfessorRoute.Planning(PlanningSubRoute.Exercises()))
                                    }
                                }
                            )
                        }
                        is PlanningSubRoute.Exercises -> {
                            if (subRoute.selectedExerciseId == null) {
                                ExerciseListScreen(
                                    onSelectExercise = { exerciseId ->
                                        viewModel.navigateToProfessorRoute(
                                            ProfessorRoute.Planning(PlanningSubRoute.Exercises(selectedExerciseId = exerciseId))
                                        )
                                    }
                                )
                            } else {
                                ExerciseDetailScreen(
                                    exerciseId = subRoute.selectedExerciseId,
                                    onBack = {
                                        viewModel.navigateToProfessorRoute(
                                            ProfessorRoute.Planning(PlanningSubRoute.Exercises(selectedExerciseId = null))
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
                is ProfessorRoute.Activity -> {
                    RoutePlaceholderScreen(
                        badgeText = "ÁREA DO PROFESSOR",
                        title = "Histórico de Atividades",
                        description = "Acompanhe atualizações, treinos concluídos e mensagens enviadas pelos alunos."
                    )
                }
                is ProfessorRoute.Profile -> {
                    RoutePlaceholderScreen(
                        badgeText = "ÁREA DO PROFESSOR",
                        title = "Seu Perfil Profissional",
                        description = "Configurações da conta, tema, notificações e encerramento de sessão.",
                        onLogout = { viewModel.logout() }
                    )
                }
            }
        }
    }
}
