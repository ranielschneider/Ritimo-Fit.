package com.ranielschneider.ritimofit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ranielschneider.ritimofit.core.result.AppResult
import com.ranielschneider.ritimofit.domain.model.AssignedWorkoutDetails
import com.ranielschneider.ritimofit.domain.model.CompletedWorkoutDetails
import com.ranielschneider.ritimofit.domain.model.User
import com.ranielschneider.ritimofit.domain.model.UserRole
import com.ranielschneider.ritimofit.domain.model.WorkoutCompletion
import com.ranielschneider.ritimofit.domain.repository.AuthRepository
import com.ranielschneider.ritimofit.domain.usecase.CompleteWorkoutUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetStudentWorkoutAssignmentsUseCase
import com.ranielschneider.ritimofit.domain.usecase.GetWorkoutCompletionsForStudentUseCase
import com.ranielschneider.ritimofit.presentation.base.UiState
import com.ranielschneider.ritimofit.presentation.navigation.WorkoutsSubRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StudentWorkoutsViewModel(
    private val authRepository: AuthRepository,
    private val getStudentWorkoutAssignmentsUseCase: GetStudentWorkoutAssignmentsUseCase,
    private val completeWorkoutUseCase: CompleteWorkoutUseCase,
    private val getWorkoutCompletionsForStudentUseCase: GetWorkoutCompletionsForStudentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<AssignedWorkoutDetails>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<AssignedWorkoutDetails>>> = _uiState.asStateFlow()

    private val _completionsState = MutableStateFlow<List<WorkoutCompletion>>(emptyList())
    val completionsState: StateFlow<List<WorkoutCompletion>> = _completionsState.asStateFlow()

    private val _historyUiState = MutableStateFlow<UiState<List<CompletedWorkoutDetails>>>(UiState.Idle)
    val historyUiState: StateFlow<UiState<List<CompletedWorkoutDetails>>> = _historyUiState.asStateFlow()

    private val _selectedSubTab = MutableStateFlow<WorkoutsSubRoute>(WorkoutsSubRoute.TodayWorkout())
    val selectedSubTab: StateFlow<WorkoutsSubRoute> = _selectedSubTab.asStateFlow()

    init {
        loadStudentWorkouts()
    }

    fun loadStudentWorkouts() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _historyUiState.value = UiState.Loading

            val currentUserResult = authRepository.getCurrentSessionUser()
            val studentUser = when (currentUserResult) {
                is AppResult.Success -> currentUserResult.data
                is AppResult.Error -> {
                    User(
                        id = "std_1",
                        email = "aluno1@ritimofit.com",
                        fullName = "Carlos Aluno",
                        role = UserRole.STUDENT
                    )
                }
            }

            // Observe completions list for status check
            launch {
                getWorkoutCompletionsForStudentUseCase.observeCompletions(studentUser.id).collectLatest { result ->
                    if (result is AppResult.Success) {
                        _completionsState.value = result.data
                    }
                }
            }

            // Observe completed details for History tab
            launch {
                getWorkoutCompletionsForStudentUseCase.observeCompletedDetails(studentUser.id).collectLatest { result ->
                    _historyUiState.value = when (result) {
                        is AppResult.Success -> UiState.Success(result.data)
                        is AppResult.Error -> UiState.Error(result.error)
                    }
                }
            }

            getStudentWorkoutAssignmentsUseCase(studentUser.id).collectLatest { result ->
                _uiState.value = when (result) {
                    is AppResult.Success -> UiState.Success(result.data)
                    is AppResult.Error -> UiState.Error(result.error)
                }
            }
        }
    }

    fun completeWorkout(workoutId: String) {
        viewModelScope.launch {
            completeWorkoutUseCase(workoutId, _completionsState.value)
        }
    }

    fun onSubTabSelected(subRoute: WorkoutsSubRoute) {
        _selectedSubTab.value = subRoute
    }
}
